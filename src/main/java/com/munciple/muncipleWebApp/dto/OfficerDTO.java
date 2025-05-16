package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OfficerDTO {
    private Long officerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String role;
    private String assignedZone;
    private String departmentName;
    private Long departmentId;
}
