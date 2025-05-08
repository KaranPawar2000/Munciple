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

    @Scheduled(fixedRate = 3600000) // Runs every hour
    @Transactional
    public void escalateUnresolvedComplaints() {
        LocalDateTime fortyHoursAgo = LocalDateTime.now().minusHours(10);
        List<Complaint> unresolvedComplaints = complaintRepository.findUnresolvedComplaints(fortyHoursAgo)
                .stream()
                .filter(complaint -> !"Resolved".equals(complaint.getStatus()))
                .toList();
         System.out.println("Unresolved complaints: " + unresolvedComplaints.size());

        if (!unresolvedComplaints.isEmpty()) {
            for (Complaint complaint : unresolvedComplaints) {
                if ("Esc_1".equals(complaint.getStatus())) {
                    Officer escalationOfficer = officerRepository.findByDepartmentAndRole(complaint.getDepartment(), "3")
                            .orElseThrow(() -> new RuntimeException("Escalation officer with role 3 not found for department " + complaint.getDepartment().getDepartmentName()));

                    complaint.setAssignedOfficer(escalationOfficer);
                    complaint.setStatus("Esc_2");
                    complaintRepository.save(complaint);

                    ComplaintStatus complaintStatus = new ComplaintStatus();
                    complaintStatus.setComplaint(complaint);
                    complaintStatus.setRemarks("Assigned to Senior Officer");
                    complaintStatus.setStatus("Esc_2");
                    complaintStatus.setUpdatedBy(escalationOfficer);
                    complaintStatus.setUpdatedAt(LocalDateTime.now());
                    complaintStatusRepository.save(complaintStatus);

                    Escalation escalation = new Escalation();
                    escalation.setComplaint(complaint);
                    escalation.setEscalatedTo(escalationOfficer);
                    escalation.setReason("Query not solved");
                    escalation.setEscalationLevel(2);
                    escalation.setEscalatedAt(LocalDateTime.now());
                    escalationRepository.save(escalation);

                } else if("Assigned".equals(complaint.getStatus())){
                    Officer escalationOfficer = officerRepository.findByDepartmentAndRole(complaint.getDepartment(), "2")
                            .orElseThrow(() -> new RuntimeException("Escalation officer with role 2 not found for department " + complaint.getDepartment().getDepartmentName()));

                    complaint.setAssignedOfficer(escalationOfficer);
                    complaint.setStatus("Esc_1");
                    complaintRepository.save(complaint);

                    ComplaintStatus complaintStatus = new ComplaintStatus();
                    complaintStatus.setComplaint(complaint);
                    complaintStatus.setRemarks("Assigned to Manager");
                    complaintStatus.setStatus("Esc_1");
                    complaintStatus.setUpdatedBy(escalationOfficer);
                    complaintStatus.setUpdatedAt(LocalDateTime.now());
                    complaintStatusRepository.save(complaintStatus);

                    Escalation escalation = new Escalation();
                    escalation.setComplaint(complaint);
                    escalation.setEscalatedTo(escalationOfficer);
                    escalation.setReason("Query not solved");
                    escalation.setEscalationLevel(1);
                    escalation.setEscalatedAt(LocalDateTime.now());
                    escalationRepository.save(escalation);
                }
            }
        }
    }
}