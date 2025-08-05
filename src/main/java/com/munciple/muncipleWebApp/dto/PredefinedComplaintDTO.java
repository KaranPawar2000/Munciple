package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PredefinedComplaintDTO {
    private Long id;
    private String name;
    private String description;
    private String marathiName;
    private String marathiDescription;
}
