package com.munciple.muncipleWebApp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FeedbackDTO {
    private Long complaintId;
    private String whatsappId;
    private int rating;
    private String comments;
}
