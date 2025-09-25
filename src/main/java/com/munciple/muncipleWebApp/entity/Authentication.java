package com.munciple.muncipleWebApp.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Authentication")
@Getter
@Setter
@NoArgsConstructor
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long authenticationId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


}
