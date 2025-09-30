package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllOfficerDTO {
    private Long officerId;
    private String name;
    private String marathiName;
    private String designation;
    private String email;
    private String phoneNumber;
    private String role;
    private boolean status;
}
