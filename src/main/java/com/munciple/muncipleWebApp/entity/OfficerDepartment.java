package com.munciple.muncipleWebApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Officer_Departments")
@Getter
@Setter
@NoArgsConstructor
public class OfficerDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private Officer officer;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private MunicipalDepartment department;

    private String wardNumber;

}
