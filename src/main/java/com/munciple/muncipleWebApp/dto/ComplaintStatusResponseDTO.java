package com.munciple.muncipleWebApp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintStatusResponseDTO {

    private String message;
    private OfficerDTO officer;
}
