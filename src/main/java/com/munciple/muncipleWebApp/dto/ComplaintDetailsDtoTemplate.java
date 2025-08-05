package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDetailsDtoTemplate {

        private String officerName;
        private String officerMarathiName;
        private String complaintId;
        private String departmentId;
        private String category;
        private String description;
        private String locationUrl;
        private String imageUrl;
        private String officerPhone;

        public ComplaintDetailsDtoTemplate(
                String officerName,
                String officerMarathiName,
                String complaintId,
                String category,
                String description,
                String locationUrl,
                String imageUrl,
                String officerPhone) {
                this.officerName = officerName;
                this.officerMarathiName = officerMarathiName;
                this.complaintId = complaintId;
                this.category = category;
                this.description = description;
                this.locationUrl = locationUrl;
                this.imageUrl = imageUrl;
                this.officerPhone = officerPhone;
        }
}

