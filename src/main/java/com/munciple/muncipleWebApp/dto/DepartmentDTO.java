package com.munciple.muncipleWebApp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    long id;
    String name;
    String marathiName;
    String city;
    boolean status;
}
