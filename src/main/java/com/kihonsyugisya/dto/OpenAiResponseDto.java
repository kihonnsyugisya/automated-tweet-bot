package com.kihonsyugisya.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OpenAiResponseDto {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;

        @Data
        @AllArgsConstructor
        public static class Message {
            private String role;
            private String content;
        }
    }
}
