package com.munciple.muncipleWebApp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsDTO {
    private long id;
    private String category;
    private String description;
    private String status;
    private String createdAt;
    private String lastUpdatedAt;  // Add this field
    private String longitude;
    private String latitude;
    private String userName;
    private String phoneNumber;
    private String whatsappId;
    private String imageUrl;  // Add this field
    private String locationUrl;  // Add this field

    public ComplaintDetailsDTO(Long complaintId, String category, String description, String status, String string, String longitude, String latitude, String name, String phoneNumber, String whatsappId) {
    }
}
