package com.munciple.muncipleWebApp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsDTO {
    private String category;
    private String description;
    private String status;
    private String createdAt;
    private String longitude;
    private String latitude;
    private String userName;
    private String phoneNumber;
    private String whatsappId;
}
