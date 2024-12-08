package com.kihonsyugisya.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OpenAiRequestDto {
    private String model;
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role; 
        private String content;
    }
}
