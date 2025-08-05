package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "ward_number",nullable = false)
    private String wardNumber;

    @Column(name = "whatsapp_id", unique = true)
    private String whatsappId;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;
    private String state;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "marathi_name")
    private String MarathiName;

    private Long IsEnglishMarathi;

}
