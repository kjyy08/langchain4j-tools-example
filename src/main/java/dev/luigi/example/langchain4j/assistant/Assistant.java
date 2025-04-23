package dev.luigi.example.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * LLM 기반 어시스턴트 인터페이스.
 * <p>
 * 이 인터페이스는 langchain4j를 사용하여 LLM과 상호작용하는 메서드를 정의합니다.
 * 데이터베이스 참조 기능을 통합하려면 이 인터페이스의 구현체에서 Tool 클래스를 활용하거나
 * 프롬프트에 데이터베이스 정보를 포함시킬 수 있습니다.
 * </p>
 */
@AiService
public interface Assistant {
    /**
     * 사용자 메시지에 대한 응답을 생성합니다.
     * <p>
     * 이 메서드는 단일 응답을 반환하며, 데이터베이스 참조를 위해 Tool 클래스와 함께 사용될 수 있습니다.
     * </p>
     *
     * @param message 사용자 입력 메시지
     * @return LLM이 생성한 응답 문자열
     */
    @SystemMessage("""
            use korean.
            use markdown.
            """)
    String chat(@UserMessage String message);

    /**
     * 사용자 메시지에 대한 스트리밍 응답을 생성합니다.
     * <p>
     * 이 메서드는 토큰 스트림을 반환하여 실시간으로 응답을 표시할 수 있습니다.
     * 데이터베이스 참조를 위해 Tool 클래스와 함께 사용될 수 있습니다.
     * </p>
     *
     * @param message 사용자 입력 메시지
     * @return LLM이 생성한 응답의 토큰 스트림
     */
    @SystemMessage("""
            use korean.
            use markdown.
            """)
    TokenStream streamChat(@UserMessage String message);
}
