package dev.luigi.example.langchain4j.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {
    /**
     * 채팅 기록을 저장하는 메모리를 생성합니다.
     * <p>
     * 최대 15개의 메시지를 저장하는 윈도우 방식의 채팅 메모리를 사용합니다.
     * </p>
     *
     * @return 채팅 메모리 인스턴스
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(15);
    }
}
