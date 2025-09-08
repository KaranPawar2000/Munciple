package com.munciple.muncipleWebApp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsDTO {
    private long id;
    private String Englishcategory;
    private String MarathiCategory;
    private String description;
    private String status;
    private String createdAt;
    private String lastUpdatedAt;  // Add this field
    private String longitude;
    private String latitude;
    private String userName;
    private String whatsappId;
    private String imageUrl;  // Add this field
    private String resolvedImageUrl;  // Add this field
    private String locationUrl;  // Add this field
    private Integer escalationLevel; // Add this field
    private String lastEscalatedOfficerName;
    private String lastEsclatedOfficerDesignation;// New field
    private String lastEscalatedOfficerPhone;
}


//public class ComplaintDetailsDTO {
//    private long id;
//    private String category;
//    private String description;
//    private String status;
//    private String createdAt;
//    private String lastUpdatedAt;  // Add this field
//    private String longitude;
//    private String latitude;
//    private String userName;
//    private String whatsappId;
//    private String imageUrl;  // Add this field
//    private String resolvedImageUrl;  // Add this field
//    private String locationUrl;  // Add this field
//    private Integer escalationLevel; // Add this field
//    private String lastEscalatedOfficerName;
//    private String lastEsclatedOfficerDesignation;// New field
//    private String lastEscalatedOfficerPhone;
//    }

