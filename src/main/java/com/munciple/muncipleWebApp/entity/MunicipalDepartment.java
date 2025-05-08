package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Municipal_Departments")
@Getter
@Setter
@NoArgsConstructor
public class MunicipalDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name", unique = true, nullable = false)
    private String departmentName;

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredefinedComplaint> predefinedComplaints; // List of predefined complaints

}
