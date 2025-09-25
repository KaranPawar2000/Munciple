package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Officers")
@Getter
@Setter
@NoArgsConstructor
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officerId;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String designation;

    private String role; // "1", "2", "3"

    @OneToMany(mappedBy = "officer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfficerDepartment> departments;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private String marathiName;

    @Column(name = "status", nullable = false)
    private boolean status;

}