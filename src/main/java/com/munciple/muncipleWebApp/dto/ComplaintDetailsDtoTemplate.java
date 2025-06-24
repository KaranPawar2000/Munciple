package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsDtoTemplate {

        private String officerName;
        private String complaintId;
        private String departmentId;
        private String category;
        private String description;
        private String locationUrl;
        private String imageUrl;
        private String phone;

        public ComplaintDetailsDtoTemplate(String name, String s, String category, String description, String phoneNumber) {
        }
}

