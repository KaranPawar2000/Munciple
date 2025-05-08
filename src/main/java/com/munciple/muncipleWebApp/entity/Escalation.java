package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Escalations")
@Getter
@Setter
@NoArgsConstructor
public class Escalation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "escalation_id")
    private Long escalationId;

    @ManyToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne
    @JoinColumn(name = "escalated_to", nullable = false)
    private Officer escalatedTo;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "escalation_level", nullable = false)
    private int escalationLevel ;

    @Column(name = "escalated_at", updatable = false)
    private LocalDateTime escalatedAt = LocalDateTime.now();
}
