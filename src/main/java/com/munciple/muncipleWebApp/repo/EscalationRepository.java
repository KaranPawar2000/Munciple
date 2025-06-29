package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.Complaint;
import com.munciple.muncipleWebApp.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Optional<Escalation> findTopByComplaintOrderByEscalatedAtDesc(Complaint complaint);
}
