package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data // Lombok for getters, setters, toString, etc.
@NoArgsConstructor

public class WhatsAppMessageRequest {

    private String from;
    private String to;
    private String type;
    private Message message;

    public WhatsAppMessageRequest(String from, String to, String type, Message message) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.message = message;
    }

    @Data
    @AllArgsConstructor
    public static class Message {
        private String templateid;
        private List<String> placeholders;
    }
}
