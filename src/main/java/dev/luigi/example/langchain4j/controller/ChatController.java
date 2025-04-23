package dev.luigi.example.langchain4j.controller;

import dev.luigi.example.langchain4j.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * LLM 채팅 기능을 위한 REST 컨트롤러.
 * <p>
 * 이 컨트롤러는 langchain4j를 사용한 LLM 채팅 기능의 API 엔드포인트를 제공합니다.
 * 데이터베이스 참조 기능이 구현된 채팅 API를 통해 사용자는 애플리케이션의 데이터에
 * 기반한 응답을 받을 수 있습니다.
 * </p>
 */
@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * 일반 채팅 엔드포인트.
     * <p>
     * 이 엔드포인트는 사용자 프롬프트에 대한 단일 응답을 반환합니다.
     * </p>
     *
     * @param prompt 사용자 입력 메시지
     * @return LLM 응답 문자열
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return chatService.chat(prompt);
    }

    /**
     * 어시스턴트 스트리밍 엔드포인트.
     * <p>
     * 이 엔드포인트는 어시스턴트 모드에서 사용자 프롬프트에 대한 응답을 스트리밍 방식으로 반환합니다.
     * 참고: 이 메서드는 ChatService에 streamAssistant 메서드가 구현되어 있어야 합니다.
     * </p>
     *
     * @param prompt 사용자 입력 메시지
     * @return LLM 응답 스트림 (Flux<String>)
     */
    @GetMapping(value = "/stream-chat", produces = "text/event-stream")
    public Flux<String> streamAssistant(@RequestParam String prompt) {
        return chatService.streamChat(prompt);
    }
}
