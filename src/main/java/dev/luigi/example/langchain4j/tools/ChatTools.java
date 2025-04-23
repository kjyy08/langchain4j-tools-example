package dev.luigi.example.langchain4j.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.luigi.example.recipe.entity.Recipe;
import dev.luigi.example.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * LLM이 데이터베이스에 접근할 수 있는 도구(Tool) 클래스.
 * <p>
 * 이 클래스는 langchain4j의 Tool 어노테이션을 사용하여 LLM이 데이터베이스에 접근할 수 있는 
 * 메서드를 정의합니다. 각 메서드는 특정 데이터베이스 작업을 수행하며, 
 * LLM은 사용자 쿼리에 따라 적절한 도구를 선택하여 호출합니다.
 * </p>
 * <p>
 * 데이터베이스 참조 기능을 확장하려면 이 클래스에 새로운 Tool 메서드를 추가하면 됩니다.
 * 예를 들어, 사용자 관련 테이블에 접근하는 도구를 추가할 수 있습니다.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatTools {
    private final RecipeService recipeService;

    /**
     * 모든 레시피를 조회하는 도구.
     * <p>
     * 이 도구는 데이터베이스에서 모든 레시피 정보를 가져와 LLM에게 제공합니다.
     * LLM은 이 정보를 사용하여 사용자 질문에 답변할 수 있습니다.
     * </p>
     *
     * @return 모든 레시피 목록
     */
    @Tool("""
            다음과 같은 상황에서 이 도구를 사용하세요:
            1. 사용자가 '어떤 레시피들이 있나요?'와 비슷한 질문을 할 때
            2. 사용자가 '가지고 있는 모든 레시피를 보여주세요'라고 요청할 때
            3. 사용자가 전체 레시피 목록이나 메뉴 리스트를 요청할 때

            이 도구는 데이터베이스에 저장된 모든 레시피의 목록을 반환합니다.
            각 레시피에는 다음 정보가 포함됩니다:
            - 요리 이름
            - 요리 설명

            주의: 특정 레시피나 정보를 찾는 경우에는 이 도구를 사용하지 마세요.
            단순히 전체 레시피 목록이 필요할 때만 사용하세요.
            """)
    public List<Recipe> findAllRecipes() {
        log.info("findAllRecipes() called.");
        return recipeService.findAll();
    }

    /**
     * 이름으로 특정 레시피를 조회하는 도구.
     * <p>
     * 이 도구는 사용자가 특정 요리에 대한 정보를 요청할 때 사용됩니다.
     * 레시피 이름을 매개변수로 받아 해당 레시피 정보를 데이터베이스에서 조회합니다.
     * </p>
     *
     * @param name 조회할 레시피 이름
     * @return 조회된 레시피 정보 (없으면 null)
     */
    @Tool("""
            사용자가 음식이나 요리 관련 정보를 요청할 때 사용하세요.
            다음과 같은 상황에서 이 도구를 사용하세요:
            - 특정 요리의 레시피나 조리법을 찾을 때

            데이터베이스에 저장된 레시피 정보를 반환하며, 각 레시피에는 다음 정보가 포함됩니다:
            - ID
            - 요리 이름
            - 요리 설명
            """)
    public Recipe findRecipeByName(@P("특정 요리에 대한 레시피") String name) {
        log.info("recipeService.findByName({}) called.", name);
        return recipeService.findAll().stream()
                .filter(recipe -> recipe.getName().equals(name))
                .findFirst()
                .orElse(null);
    }


    @Tool("""
            사용자가 특정 레시피를 지워달라고 하면 다음 도구를 사용하세요
            """)
    public void deleteRecipeByName(@P("특정 레시피 요리 이름") String name){
        recipeService.deleteByName(name);
    }
}
