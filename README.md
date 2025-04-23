# Langchain4j와 데이터베이스 연동 가이드

이 문서는 langchain4j를 사용하여 LLM(Large Language Model) 응답에 데이터베이스 참조를 가능하게 하는 방법을 설명합니다.

## 목차

1. [개요](#개요)
    - [환경변수](#환경변수-설정)
2. [데이터베이스 참조 방법](#데이터베이스-참조-방법)
    - [도구 사용(Tool Usage)](#도구-사용tool-usage)
3. [유저 테이블 추가 예시](#유저-테이블-추가-예시)
    - [사용자 엔티티 생성](#사용자-엔티티-생성)
    - [사용자 리포지토리 생성](#사용자-리포지토리-생성)
    - [사용자 서비스 생성](#사용자-서비스-생성)
    - [사용자 관련 도구 추가](#사용자-관련-도구-추가)

## 개요

langchain4j는 Java 애플리케이션에서 LLM을 쉽게 통합할 수 있는 프레임워크입니다.  
이 가이드에서는 langchain4j를 사용하여 LLM이 데이터베이스의 정보를 참조하여 응답할 수 있도록 하는 방법을
설명합니다.

### 환경변수 설정

```properties
# 데이터베이스 연결 정보
DB_URL=your_database_url
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

# Google AI Gemini API 설정
GEMINI_API_KEY=your_gemini_api_key
GEMINI_MODEL=gemini_model_name
```

위의 환경변수는 프로젝트의 파일에서 참조되며, 각각 다음과 같은 역할을 합니다.

- DB_URL, DB_USERNAME, DB_PASSWORD: 데이터베이스 연결 정보
- GEMINI_API_KEY: Google Gemini AI API 접근 키
- GEMINI_MODEL: 채팅에 사용할 Gemini 모델명

## 데이터베이스 참조 방법

LLM이 데이터베이스 정보를 참조할 수 있게 하는 방법으로 도구를 소개합니다.

### 도구 사용(Tool Usage)

LLM이 다양한 외부 작업을 필요할 때 사용하도록 `langchain4j`에서는 이를 위한 도구를 손쉽게 정의할 수 있습니다.

#### 구현 단계:

1. Tool 어노테이션이 있는 메서드 정의
2. 메서드 내에서 데이터베이스 액세스 로직 구현
3. AiService 어노테이션이 있는 Assistant 인터페이스 정의

#### 예제 코드:

```java
@Component
public class ChatTools {
    private final RecipeService recipeService;

    @Tool("이 도구는 데이터베이스에 저장된 모든 레시피의 목록을 반환합니다.")
    public List<Recipe> findAllRecipes() {
        return recipeService.findAll();
    }

    @Tool("이 도구는 특정 이름의 레시피를 검색합니다.")
    public Recipe findRecipeByName(@P("레시피 이름") String name) {
        return recipeService.findAll().stream()
                .filter(recipe -> recipe.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}

@AiService
public interface Assistant {
   @SystemMessage("""
           당신은 LLM 챗봇입니다.
           유저에게 필요한 응답을 친절하고 매너있게 응답해주세요.
           """)
   String chat(@UserMessage String message);
}
```

## 유저 테이블 추가 예시

이 섹션에서는 사용자 관련 테이블을 추가하고 langchain4j와 연동하는 방법을 설명합니다.

### 사용자 엔티티 생성

먼저 사용자 엔티티를 생성합니다.

```java

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String fullName;

    @Column(length = 1000)
    private String bio;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### 사용자 리포지토리 생성

사용자 데이터에 접근하기 위한 리포지토리를 생성합니다.

```java

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByFullNameContaining(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
```

### 사용자 서비스 생성

사용자 관련 비즈니스 로직을 처리할 서비스를 생성합니다.

```java

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findUsersByName(String name) {
        return userRepository.findByFullNameContaining(name);
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### 사용자 관련 도구 추가

LLM이 사용자 데이터에 접근할 수 있는 도구를 추가합니다.

```java

@Component
@RequiredArgsConstructor
public class UserTools {
    private final UserService userService;

    @Tool("""
            다음과 같은 상황에서 이 도구를 사용하세요:
            1. 사용자가 '어떤 사용자들이 있나요?'와 비슷한 질문을 할 때
            2. 사용자가 '모든 사용자 목록을 보여주세요'라고 요청할 때
            
            이 도구는 데이터베이스에 저장된 모든 사용자의 목록을 반환합니다.
            각 사용자에는 다음 정보가 포함됩니다:
            - 사용자 이름
            - 이메일
            - 전체 이름
            - 자기소개
            """)
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @Tool("""
            사용자가 특정 사용자에 대한 정보를 요청할 때 사용하세요.
            다음과 같은 상황에서 이 도구를 사용하세요:
            - 특정 사용자 이름으로 사용자 정보를 찾을 때
            
            데이터베이스에 저장된 사용자 정보를 반환합니다.
            """)
    public User findUserByUsername(@P("찾을 사용자의 이름") String username) {
        return userService.findUserByUsername(username).orElse(null);
    }

    @Tool("""
            사용자가 이름으로 사용자를 검색할 때 사용하세요.
            다음과 같은 상황에서 이 도구를 사용하세요:
            - 특정 이름을 포함하는 사용자를 찾을 때
            
            이름에 검색어를 포함하는 모든 사용자의 목록을 반환합니다.
            """)
    public List<User> findUsersByName(@P("검색할 이름") String name) {
        return userService.findUsersByName(name);
    }
}
```