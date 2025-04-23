package dev.luigi.example.langchain4j.service;

import dev.luigi.example.langchain4j.assistant.Assistant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 * LLM 기반 채팅 서비스.
 * <p>
 * 이 서비스는 langchain4j의 Assistant 인터페이스를 사용하여 채팅 기능을 제공합니다.
 * 데이터베이스 참조 기능을 구현하려면 다음과 같은 방법을 사용할 수 있습니다:
 * - 도구 사용(Tool Usage): langchain4j의 Tool 어노테이션을 사용하여 데이터베이스 액세스 메서드 정의
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final Assistant assistant;

    /**
     * 단일 응답으로 채팅 메시지를 처리합니다.
     *
     * @param prompt 사용자 입력 메시지
     * @return AI 응답 메시지
     */
    public String chat(String prompt) {
        String result = assistant.chat(prompt);
        log.info("Chat result: {}", result);
        return result;
    }

    /**
     * Assistant 객체를 사용하여 스트리밍 방식으로 채팅 응답을 처리합니다.
     * 메소드 체이닝 방식으로 이벤트 핸들러를 등록합니다.
     *
     * @param prompt 사용자 입력 메시지
     * @return 스트리밍 AI 응답 (Flux<String>)
     */
    public Flux<String> streamChat(String prompt) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        assistant.streamChat(prompt)
                .onPartialResponse(response -> {
                    log.info("Assistant partial response: {}", response);
                    sink.tryEmitNext(response);
                })
                .onCompleteResponse(c -> {
                    log.info("Assistant streaming completed");
                    sink.tryEmitComplete();
                })
                .onError(error -> {
                    log.error("Assistant streaming error: {}", error.getMessage(), error);
                    sink.tryEmitError(error);
                })
                .start();

        return sink.asFlux().timeout(Duration.ofSeconds(30));
    }
}
