package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintEscalationResponseDTO {
    private String departmentName;
    private String description;
    private String wardNumber;
    private String citizenName;
    private String citizenContact;
    private String resolvedDate;
    private String complaintImageUrl;
    private String resolvedImageUrl;
    private String locationUrl;
    private List<EscalationDTO> escalationHistory;
}