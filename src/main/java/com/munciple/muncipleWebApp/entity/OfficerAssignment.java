package com.munciple.muncipleWebApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Officer_Assignments")
@Getter
@Setter
@NoArgsConstructor
public class OfficerAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private Officer officer;

    @Column(name = "ward_number", nullable = false)
    private String wardNumber;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private MunicipalDepartment department;

    @Column(name = "role", nullable = false)
    private String role; // 1, 2, or 3

    @Column(name = "assigned_zone")
    private String assignedZone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

