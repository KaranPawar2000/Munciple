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

    @Scheduled(fixedRate = 10000) // Runs every 48 hours
    @Transactional
    public void escalateUnresolvedComplaints() {
        LocalDateTime now = LocalDateTime.now();

        // Get all unresolved complaints that have passed their estimated time
        List<Complaint> unresolvedComplaints = complaintRepository.findAllUnresolvedComplaints()
                .stream()
                .filter(complaint ->
                        complaint.getEstimatedTime() != null &&
                                now.isAfter(complaint.getEstimatedTime()) &&
                                !"Resolved".equals(complaint.getStatus()))
                .toList();

        if (!unresolvedComplaints.isEmpty()) {
            for (Complaint complaint : unresolvedComplaints) {
                // Get the latest escalation for this complaint
                Escalation latestEscalation = escalationRepository
                        .findTopByComplaintOrderByEscalatedAtDesc(complaint)
                        .orElse(null);

                if (latestEscalation == null) {
                    // If no escalation exists, create first level escalation
                    createNewEscalation(complaint, 1, 24); // 24 hours for level 1
                } else {
                    int currentLevel = latestEscalation.getEscalationLevel();
                    if (currentLevel == 1) {
                        // Escalate to level 2 and assign to officer with role 2
                        createNewEscalation(complaint, 2, 36); // 36 hours for level 2
                    } else if (currentLevel == 2) {
                        // Escalate to level 3 and assign to officer with role 3
                        createNewEscalation(complaint, 3, 48); // 48 hours for level 3
                    }
                }
            }
        }
    }

    private void createNewEscalation(Complaint complaint, int level, int estimatedHours) {
        // Find officer with the corresponding role in the same department
        String role = String.valueOf(level);
        Officer escalationOfficer = officerRepository.findByDepartmentAndRole(complaint.getDepartment(), role)
                .orElseThrow(() -> new RuntimeException("Escalation officer with role " + role +
                        " not found for department " + complaint.getDepartment().getDepartmentName()));

        // Create new escalation record
        Escalation newEscalation = new Escalation();
        newEscalation.setComplaint(complaint);
        newEscalation.setEscalatedAt(LocalDateTime.now());
        newEscalation.setReason("Complaint not resolved within estimated time");
        newEscalation.setEscalationLevel(level);
        newEscalation.setEscalatedTo(escalationOfficer);
        escalationRepository.save(newEscalation);

        // Update complaint
        complaint.setAssignedOfficer(escalationOfficer);
        complaint.setEstimatedTime(complaint.getCreatedAt().plusHours(estimatedHours));
        complaintRepository.save(complaint);
    }


}