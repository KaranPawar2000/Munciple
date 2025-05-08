package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
}
