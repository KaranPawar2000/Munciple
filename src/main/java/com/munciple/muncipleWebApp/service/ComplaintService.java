    package com.munciple.muncipleWebApp.service;


    import com.munciple.muncipleWebApp.dto.*;
    import org.springframework.http.*;
    import org.springframework.web.client.RestTemplate;

    import com.munciple.muncipleWebApp.entity.*;
    import com.munciple.muncipleWebApp.repo.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;


    @Service
    public class ComplaintService {

        @Autowired
        private ComplaintRepository complaintRepository;

        @Autowired
        private ComplaintStatusRepository complaintStatusRepository;

        @Autowired
        private OfficerRepository officerRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MunicipalDepartmentRepository departmentRepository;

        @Autowired
        private PredefinedComplaintRepository predefinedComplaintRepository;


        public ComplaintDetailsDtoTemplate  registerComplaint(Request request) {
            Officer assignedOfficer = officerRepository.findFirstByDepartment_DepartmentIdAndRole(request.getDepartmentId(), "1")
                    .orElseThrow(() -> new RuntimeException("No officer found with role 1 in the department"));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MunicipalDepartment department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            Complaint complaint = new Complaint();
            complaint.setCategory(request.getCategory());
            complaint.setDescription(request.getDescription());
            complaint.setImageUrl(request.getImageUrl());
            complaint.setDepartment(department);
            complaint.setUser(user);
            complaint.setAssignedOfficer(assignedOfficer);
            complaint.setStatus("Assigned");
            complaint.setCreatedAt(LocalDateTime.now());
            complaint.setLongitude(request.getLongitude());
            complaint.setLatitude(request.getLatitude());
            Complaint savedComplaint = complaintRepository.save(complaint);

            ComplaintStatus status = new ComplaintStatus();
            status.setComplaint(savedComplaint);
            status.setStatus("Assigned");
            status.setUpdatedBy(assignedOfficer);
            status.setRemarks("Assigned to junior officer");
            status.setUpdatedAt(LocalDateTime.now());

            complaintStatusRepository.save(status);


            // Prepare response DTO
            String imageUrl = savedComplaint.getImageUrl() != null ? savedComplaint.getImageUrl() : "No image";
            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
                    savedComplaint.getLatitude() + "," + savedComplaint.getLongitude();

            return new ComplaintDetailsDtoTemplate(
                    savedComplaint.getAssignedOfficer().getName(),
                    String.valueOf(savedComplaint.getComplaintId()),
                    savedComplaint.getCategory(),
                    savedComplaint.getDescription(),
                    locationUrl,
                    imageUrl,
                    assignedOfficer.getPhoneNumber()
            );
        }



//        public void registerComplaint(Request request) {
//            Officer assignedOfficer = officerRepository.findFirstByDepartment_DepartmentIdAndRole(request.getDepartmentId(), "1")
//                    .orElseThrow(() -> new RuntimeException("No officer found with role 1 in the department"));
//
//            User user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            MunicipalDepartment department = departmentRepository.findById(request.getDepartmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//
//            Complaint complaint = new Complaint();
//            complaint.setCategory(request.getCategory());
//            complaint.setDescription(request.getDescription());
//            complaint.setImageUrl(request.getImageUrl());
//            complaint.setDepartment(department);
//            complaint.setUser(user);
//            complaint.setAssignedOfficer(assignedOfficer);
//            complaint.setStatus("Assigned");
//            complaint.setCreatedAt(LocalDateTime.now());
//            complaint.setLongitude(request.getLongitude());
//            complaint.setLatitude(request.getLatitude());
//            Complaint savedComplaint = complaintRepository.save(complaint);
//
//            ComplaintStatus status = new ComplaintStatus();
//            status.setComplaint(savedComplaint);
//            status.setStatus("Assigned");
//            status.setUpdatedBy(assignedOfficer);
//            status.setRemarks("Assigned to junior officer");
//            status.setUpdatedAt(LocalDateTime.now());
//
//            complaintStatusRepository.save(status);
//
//
//            String from = "919175781771"; // your static sender number
//            String to = assignedOfficer.getPhoneNumber(); // Officer's phone number
//            System.out.println(to);
//            String mapLink = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
//                    + request.getLatitude() + "," + request.getLongitude() + "&key=YOUR_API_KEY";
//
//            List<String> placeholders = List.of(
//                    user.getName(), // "Customer"
//                    String.valueOf(savedComplaint.getComplaintId()), // "Complaint ID"
//                    request.getCategory(), // "Category"
//                    request.getDescription(), // "Description"
//                    mapLink, // "Location"
//                    request.getImageUrl() // "Photo"
//            );
//
//            WhatsAppMessageRequest.Message message = new WhatsAppMessageRequest.Message(
//                    "807889", placeholders
//            );
//
//            WhatsAppMessageRequest whatsappRequest = new WhatsAppMessageRequest(
//                    from, to, "template", message
//            );
//            // send WhatsApp message
//            sendWhatsAppMessage(savedComplaint);
//        }


        public ComplaintDetailsDTO getComplaintDetailsById(Long complaintId) {
            Complaint complaint = complaintRepository.findById(complaintId)
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            User user = complaint.getUser();

            return new ComplaintDetailsDTO(
                    complaint.getComplaintId(),
                    complaint.getCategory(),
                    complaint.getDescription(),
                    complaint.getStatus(),
                    complaint.getCreatedAt().toString(),
                    complaint.getLongitude(),
                    complaint.getLatitude(),
                    user.getName(),
                    user.getPhoneNumber(),
                    user.getWhatsappId()
            );
        }



        public List<ComplaintDetailsDTO> getAllComplaints() {
            List<Complaint> complaints = complaintRepository.findAllUnresolvedComplaints();

            return complaints.stream().map(complaint -> {
                User user = complaint.getUser();
                return new ComplaintDetailsDTO(
                        complaint.getComplaintId(),
                        complaint.getCategory(),
                        complaint.getDescription(),
                        complaint.getStatus(),
                        complaint.getCreatedAt().toString(),
                        complaint.getLongitude(),
                        complaint.getLatitude(),
                        user.getName(),
                        user.getPhoneNumber(),
                        user.getWhatsappId()
                );
            }).collect(Collectors.toList());
        }

        public void assignComplaintToOfficer(Long complaintId, String phoneNumber) {
            System.out.println("complaintId,phoneNumber");

            Complaint complaint = complaintRepository.findById(complaintId)
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            Officer officer = officerRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("Officer not found with phone number: " + phoneNumber));

            complaint.setAssignedOfficer(officer);
            complaint.setStatus("In_Progress");

            complaintRepository.save(complaint);

            ComplaintStatus status = new ComplaintStatus();
            status.setComplaint(complaint);
            status.setStatus("In_Progress");
            status.setUpdatedBy(officer);
            status.setRemarks("Task assigned to officer");
            status.setUpdatedAt(LocalDateTime.now());

            complaintStatusRepository.save(status);
        }


        public void updateComplaintStatus(Request request) {
            Complaint complaint = complaintRepository.findById(request.getComplaintId())
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            Officer officer = officerRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Officer not found with given phone number"));

            // Update main complaint status
            complaint.setStatus(request.getStatus());
            complaintRepository.save(complaint);

            // Save complaint status history
            ComplaintStatus complaintStatus = new ComplaintStatus();
            complaintStatus.setComplaint(complaint);
            complaintStatus.setStatus(request.getStatus());
            complaintStatus.setUpdatedBy(officer);
            complaintStatus.setRemarks(request.getRemarks());
            complaintStatus.setUpdatedAt(LocalDateTime.now());

            complaintStatusRepository.save(complaintStatus);
        }

        private void sendWhatsAppMessage(Complaint complaint) {
            String apiUrl = "https://api.auurumdigital.com/v2/wamessage/sendMessage";
            String from = "919175781771"; // your registered number
            String to  = complaint.getAssignedOfficer().getPhoneNumber(); // assuming this is in correct format
            System.out.println(to);
            List<String> placeholders = List.of(
                    complaint.getAssignedOfficer().getName(),
                    String.valueOf(complaint.getComplaintId()),
                    complaint.getCategory(),
                    complaint.getDescription(),
                    "https://www.google.com/maps/search/?api=1&query=" + complaint.getLatitude() + "," + complaint.getLongitude(),
                    complaint.getImageUrl() != null ? complaint.getImageUrl() : "No image"
            );

            WhatsAppMessageRequest.Message message = new WhatsAppMessageRequest.Message("807889", placeholders);
            WhatsAppMessageRequest request = new WhatsAppMessageRequest(from, to, "template", message);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // ✅ Add apikey header
            headers.set("apikey", "d9f93ae9-fc1a-11ef-ad4f-92672d2d0c2d");

            HttpEntity<WhatsAppMessageRequest> httpRequest = new HttpEntity<>(request, headers);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpRequest, String.class);
                System.out.println("WhatsApp message sent: " + response.getBody());
            } catch (Exception e) {
                System.err.println("Failed to send WhatsApp message: " + e.getMessage());
            }
        }




        public List<PredefinedComplaintDTO>  getComplaintsByDepartmentId(Long departmentId) {
            List<PredefinedComplaint> complaints = predefinedComplaintRepository.findByDepartment_DepartmentId(departmentId);

            if (complaints.isEmpty()) {
                throw new RuntimeException("No predefined complaints found for department ID: " + departmentId);
            }

            return complaints.stream()
                    .map(c -> new PredefinedComplaintDTO(c.getId(), c.getName(), c.getDescription()))
                    .collect(Collectors.toList());
        }

    }
