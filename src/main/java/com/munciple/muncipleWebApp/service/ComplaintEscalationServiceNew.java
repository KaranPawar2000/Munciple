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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintEscalationServiceNew {

    private final ComplaintRepository complaintRepository;
    private final OfficerRepository officerRepository;
    private final ComplaintStatusRepository complaintStatusRepository;
    private final EscalationRepository escalationRepository;


    public ComplaintEscalationServiceNew(ComplaintRepository complaintRepository, OfficerRepository officerRepository, ComplaintStatusRepository complaintStatusRepository, EscalationRepository escalationRepository) {
        this.complaintRepository = complaintRepository;
        this.officerRepository = officerRepository;
        this.complaintStatusRepository = complaintStatusRepository;
        this.escalationRepository = escalationRepository;

    }

    @Scheduled(fixedRate = 86400000) // Runs every 24 hours
    @Transactional
    public void escalateUnresolvedComplaints() {
        LocalDateTime now = LocalDateTime.now();

        List<Complaint> unresolvedComplaints = complaintRepository.findAllUnresolvedComplaints()
                .stream()
                .filter(complaint ->
                        complaint.getEstimatedTime() != null &&
                                now.isAfter(complaint.getEstimatedTime()) &&
                                !("Resolved".equals(complaint.getStatus()) || "Reject".equals(complaint.getStatus()))
                )
                .toList();

        if (!unresolvedComplaints.isEmpty()) {
            System.out.println("Method called to escalate complaints");
            for (Complaint complaint : unresolvedComplaints) {
                Escalation latestEscalation = escalationRepository
                        .findTopByComplaintOrderByEscalatedAtDesc(complaint)
                        .orElse(null);

                Officer escalatedOfficer;
                if (latestEscalation == null) {
                    escalatedOfficer = createNewEscalation(complaint, 1, 48);
                } else {
                    int currentLevel = latestEscalation.getEscalationLevel();
                    if (currentLevel == 1) {
                        escalatedOfficer = createNewEscalation(complaint, 2, 96);
                    } else if (currentLevel == 2) {
                        escalatedOfficer = createNewEscalation(complaint, 3, 144);
                    } else {
                        continue;
                    }
                }

                // Send WhatsApp notification
                sendEscalationWhatsAppMessage(complaint, escalatedOfficer);
            }
        }
    }

    private void sendEscalationWhatsAppMessage(Complaint complaint, Officer escalatedOfficer) {

        String apiUrl = "https://apiv3.infinitiodigital.com/v3/819781774556453/messages";
        String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
                complaint.getLatitude() + "," + complaint.getLongitude();

        Map<String, Object> requestBody = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", escalatedOfficer.getPhoneNumber(),   // phone number of officer
                "type", "template",
                "template", Map.of(
                        "name", "new_complaint_smkc",
                        "language", Map.of("code", "en"),
                        "components", List.of(
                                Map.of(
                                        "type", "body",
                                        "parameters", List.of(
                                                Map.of("type", "text", "text", escalatedOfficer.getName()),
                                                Map.of("type", "text", "text", complaint.getComplaintId().toString()),
                                                Map.of("type", "text", "text", complaint.getDepartment().getDepartmentId()),
                                                Map.of("type", "text", "text", complaint.getCategory()),
                                                Map.of("type", "text", "text", complaint.getDescription()),
                                                Map.of("type", "text", "text", locationUrl),
                                                Map.of("type", "text", "text",
                                                        complaint.getImageUrl() != null ? complaint.getImageUrl() : "No Image"
                                                )
                                        )
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", "b22b8ae2-b409-11f0-98fc-02c8a5e042bd"); // Change key if new key is required

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            System.out.println("WhatsApp escalation message sent: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send WhatsApp escalation message: " + e.getMessage());
        }
    }



    private Officer createNewEscalation(Complaint complaint, int level, int estimatedHours) {
        String role = String.valueOf(level);

        // Find escalation officer by department, ward number and role
        Officer escalationOfficer = officerRepository
                .findFirstByDepartmentAndWardAndRole(
                        complaint.getDepartment().getDepartmentId(),
                        complaint.getWardNumber(),
                        role
                )
                .orElseThrow(() -> new RuntimeException(
                        "Escalation officer with role " + role +
                                " not found for department " + complaint.getDepartment().getDepartmentName() +
                                " and ward " + complaint.getWardNumber()
                ));

        // Create new escalation record
        Escalation newEscalation = new Escalation();
        newEscalation.setComplaint(complaint);
        newEscalation.setEscalatedAt(LocalDateTime.now());
        newEscalation.setReason("Complaint not resolved within estimated time");
        newEscalation.setEscalationLevel(level);
        newEscalation.setEscalatedTo(escalationOfficer);
        escalationRepository.save(newEscalation);

        // Update complaint with new officer and estimated time
        complaint.setAssignedOfficer(escalationOfficer);
        complaint.setEstimatedTime(complaint.getCreatedAt().plusSeconds(estimatedHours));
        complaintRepository.save(complaint);

        return escalationOfficer;
    }


//    private Officer createNewEscalation(Complaint complaint, int level, int estimatedHours) {
//        String role = String.valueOf(level);
//        Officer escalationOfficer = officerRepository.findByDepartmentAndRole(complaint.getDepartment(), role)
//                .orElseThrow(() -> new RuntimeException("Escalation officer with role " + role +
//                        " not found for department " + complaint.getDepartment().getDepartmentName()));
//
//        Escalation newEscalation = new Escalation();
//        newEscalation.setComplaint(complaint);
//        newEscalation.setEscalatedAt(LocalDateTime.now());
//        newEscalation.setReason("Complaint not resolved within estimated time");
//        newEscalation.setEscalationLevel(level);
//        newEscalation.setEscalatedTo(escalationOfficer);
//        escalationRepository.save(newEscalation);
//
//        complaint.setAssignedOfficer(escalationOfficer);
//        complaint.setEstimatedTime(complaint.getCreatedAt().plusSeconds(estimatedHours));
//        complaintRepository.save(complaint);
//
//        return escalationOfficer;
//    }


}