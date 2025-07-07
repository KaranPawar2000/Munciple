package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscalationDTO {
    private Integer escalationLevel;
    private String officerName;
    private String officerContact;
    private String escalationTime;
}
