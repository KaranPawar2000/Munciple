package com.munciple.muncipleWebApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Complaint_Status")
@Getter
@Setter
@NoArgsConstructor
public class ComplaintStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Officer updatedBy;



    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "estimatedTime", updatable = false)
    private LocalDateTime estimatedTime ;

}
