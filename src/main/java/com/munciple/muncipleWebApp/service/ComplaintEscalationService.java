package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.entity.Complaint;
import com.munciple.muncipleWebApp.entity.ComplaintStatus;
import com.munciple.muncipleWebApp.entity.Escalation;
import com.munciple.muncipleWebApp.entity.Officer;
import com.munciple.muncipleWebApp.repo.ComplaintRepository;
import com.munciple.muncipleWebApp.repo.ComplaintStatusRepository;
import com.munciple.muncipleWebApp.repo.EscalationRepository;
import com.munciple.muncipleWebApp.repo.OfficerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintEscalationService {

    private final ComplaintRepository complaintRepository;
    private final OfficerRepository officerRepository;
    private final ComplaintStatusRepository complaintStatusRepository;
    private final EscalationRepository escalationRepository;


    public ComplaintEscalationService(ComplaintRepository complaintRepository, OfficerRepository officerRepository, ComplaintStatusRepository complaintStatusRepository, EscalationRepository escalationRepository) {
        this.complaintRepository = complaintRepository;
        this.officerRepository = officerRepository;
        this.complaintStatusRepository = complaintStatusRepository;
        this.escalationRepository = escalationRepository;

    }

    @Scheduled(fixedRate = 172800000) // Runs every 48 hours
    @Transactional
    public void escalateUnresolvedComplaints() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fortyEightHoursAgo = LocalDateTime.now().minusHours(48);
        List<Complaint> unresolvedComplaints = complaintRepository.findUnresolvedComplaints(fortyEightHoursAgo)
                .stream()
                .filter(complaint -> !"Resolved".equals(complaint.getStatus()))
                .toList();

        if (!unresolvedComplaints.isEmpty()) {
            for (Complaint complaint : unresolvedComplaints) {
                // Get the latest escalation for this complaint
                Escalation latestEscalation = escalationRepository.findTopByComplaintOrderByEscalatedAtDesc(complaint)
                        .orElse(null);

                if (latestEscalation == null) {
                    // If no escalation exists, create first level escalation
                    createNewEscalation(complaint, 1);
                } else {
                    // Based on current escalation level, create next level
                    int currentLevel = latestEscalation.getEscalationLevel();
                    if (currentLevel < 3) { // Assuming max level is 3
                        createNewEscalation(complaint, currentLevel + 1);
                    }
                }
            }
        }
    }

    private void createNewEscalation(Complaint complaint, int level) {
        Escalation newEscalation = new Escalation();
        newEscalation.setComplaint(complaint);
        newEscalation.setEscalatedAt(LocalDateTime.now());
        newEscalation.setReason("Complaint not solved within 48 hours");
        newEscalation.setEscalationLevel(level);

        String role = String.valueOf(level);
        Officer escalationOfficer = officerRepository.findByDepartmentAndRole(complaint.getDepartment(), role)
                .orElseThrow(() -> new RuntimeException("Escalation officer with role " + role + " not found for department " + complaint.getDepartment().getDepartmentName()));

        newEscalation.setEscalatedTo(escalationOfficer);
        escalationRepository.save(newEscalation);
    }
}