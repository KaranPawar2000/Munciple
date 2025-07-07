package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Complaints")
@Getter
@Setter
@NoArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long complaintId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl;


    private String longitude;

    private String latitude;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private MunicipalDepartment department;

    @Column(name = "ward_number",nullable = false)
    private String wardNumber;

    @ManyToOne
    @JoinColumn(name = "assigned_officer_id")
    private Officer assignedOfficer;

    @Column(name = "status", nullable = false)
    private String status = "Pending";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "estimatedTime")
    private LocalDateTime estimatedTime ;

    @Column(name = "resolved_image_url", columnDefinition = "LONGTEXT")
    private String resolvedImageUrl;



}
