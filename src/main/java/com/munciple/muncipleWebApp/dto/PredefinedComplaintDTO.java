package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PredefinedComplaintDTO {
    private Long id;
    private String name;
    private String description;
    private String marathiName;
    private String marathiDescription;
    private Long departmentId;
    private boolean status;
    private boolean photoRequired;
}
