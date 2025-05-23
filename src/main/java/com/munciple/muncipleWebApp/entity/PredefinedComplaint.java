package com.munciple.muncipleWebApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Predefined_Complaints")
@Getter
@Setter
@NoArgsConstructor
public class PredefinedComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "predefined_complaint_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private MunicipalDepartment department;


    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

}
