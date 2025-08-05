package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.Complaint;
import com.munciple.muncipleWebApp.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Optional<Complaint> findTopByUserUserIdOrderByCreatedAtDesc(Long userId);


    @Query("SELECT c FROM Complaint c WHERE c.status != 'Resolved' AND c.createdAt <= :timeLimit")
    List<Complaint> findUnresolvedComplaints(@Param("timeLimit") LocalDateTime timeLimit);

    @Query("SELECT c FROM Complaint c WHERE c.status != 'Resolved'")
    List<Complaint> findAllUnresolvedComplaints();


    }


