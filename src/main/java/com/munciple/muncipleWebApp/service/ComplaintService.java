    package com.munciple.muncipleWebApp.service;


    import com.munciple.muncipleWebApp.dto.*;
    import org.springframework.http.*;
    import org.springframework.web.client.RestTemplate;

    import com.munciple.muncipleWebApp.entity.*;
    import com.munciple.muncipleWebApp.repo.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Optional;
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


        @Autowired
        private EscalationRepository escalationRepository;

//        public ComplaintDetailsDtoTemplate  registerComplaint(Request request) {
//            Officer assignedOfficer = officerRepository.findFirstByDepartment_DepartmentIdAndRole(request.getDepartmentId(), "1")
//                    .orElseThrow(() -> new RuntimeException("No officer found with role 1 in the department"));
//
//            User user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            MunicipalDepartment department = departmentRepository.findById(request.getDepartmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//
//            // Calculate estimated time: current time + 40 hours
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime estimatedTime = now.plusHours(40);
//
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
//            complaint.setEstimatedTime(estimatedTime);
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
//            status.setEstimatedTime(estimatedTime);
//            complaintStatusRepository.save(status);
//
//
//            // Prepare response DTO
//            String imageUrl = savedComplaint.getImageUrl() != null ? savedComplaint.getImageUrl() : "No image";
//            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
//                    savedComplaint.getLatitude() + "," + savedComplaint.getLongitude();
//
//            return new ComplaintDetailsDtoTemplate(
//                    savedComplaint.getAssignedOfficer().getName(),
//                    String.valueOf(savedComplaint.getComplaintId()),
//                    savedComplaint.getCategory(),
//                    savedComplaint.getDescription(),
//                    locationUrl,
//                    imageUrl,
//                    assignedOfficer.getPhoneNumber()
//            );
//        }

//        public ComplaintDetailsDtoTemplate registerComplaint(Request request) {
//            User user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            MunicipalDepartment department = departmentRepository.findById(request.getDepartmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//
//            /// Find senior SI officer details
//            Officer seniorSI = officerRepository.findByDepartment_DepartmentIdAndRole(request.getDepartmentId(), "senior_SI")
//                    .orElse(null);
//            String seniorSIPhone = seniorSI != null ? seniorSI.getPhoneNumber() : "Not Found";
//            String seniorSIName = seniorSI != null ? seniorSI.getName() : "Not Found";
//
//            Complaint complaint = new Complaint();
//            complaint.setCategory(request.getCategory());
//            complaint.setDescription(request.getDescription());
//            complaint.setImageUrl(request.getImageUrl());
//            complaint.setDepartment(department);
//            complaint.setUser(user);
//            complaint.setStatus("In_Progress");
//            complaint.setCreatedAt(LocalDateTime.now());
//            complaint.setLongitude(request.getLongitude());
//            complaint.setLatitude(request.getLatitude());
//            Complaint savedComplaint = complaintRepository.save(complaint);
//
//            ComplaintStatus status = new ComplaintStatus();
//            status.setComplaint(savedComplaint);
//            status.setStatus("In_Progress");
//            status.setRemarks("New complaint registered");
//            status.setUpdatedAt(LocalDateTime.now());
//            complaintStatusRepository.save(status);
//
//            // Prepare response DTO
//            String imageUrl = savedComplaint.getImageUrl() != null ? savedComplaint.getImageUrl() : "No image";
//            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
//                    savedComplaint.getLatitude() + "," + savedComplaint.getLongitude();
//
//            return new ComplaintDetailsDtoTemplate(
//                    seniorSIName, // officer name
//                    String.valueOf(savedComplaint.getComplaintId()),
//                    String.valueOf(savedComplaint.getDepartment().getDepartmentId()),
//                    savedComplaint.getCategory(),
//                    savedComplaint.getDescription(),
//                    locationUrl,
//                    imageUrl,
//                    seniorSIPhone // senior SI phone number
//                    // officer phone
//            );
//        }


        public ComplaintDetailsDtoTemplate registerComplaint(Request request) {

            Long userId = request.getUserId(); // adjust based on your Request DTO structure
            Optional<Complaint> lastComplaintOpt = complaintRepository.findTopByUserUserIdOrderByCreatedAtDesc(userId);

            if (lastComplaintOpt.isPresent()) {
                Complaint lastComplaint = lastComplaintOpt.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime oneHourAgo = now.minusHours(1);

                if (lastComplaint.getCreatedAt().isAfter(oneHourAgo)) {
                    throw new RuntimeException("You can only register a new complaint after 1 hour from your last complaint.");
                }
            }
            // Find junior officer for the department and ward
            Officer assignedOfficer = officerRepository
                    .findJuniorOfficerByDepartmentAndWard(
                            request.getDepartmentId(),
                            request.getWardNumber()
                    )
                    .orElseThrow(() -> new RuntimeException(
                            "No junior officer found for department ID: " +
                                    request.getDepartmentId() +
                                    " and ward number: " +
                                    request.getWardNumber()
                    ));

            // Find user and department
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MunicipalDepartment department = departmentRepository
                    .findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            // Create and save complaint
            Complaint complaint = new Complaint();
            complaint.setCategory(request.getCategory());
            complaint.setDescription(request.getDescription());
            complaint.setImageUrl(request.getImageUrl());
            complaint.setDepartment(department);
            complaint.setUser(user);
            complaint.setAssignedOfficer(assignedOfficer);
            complaint.setStatus("In Progress");
            complaint.setCreatedAt(LocalDateTime.now());
            complaint.setEstimatedTime(LocalDateTime.now().plusSeconds(24));
            complaint.setLongitude(request.getLongitude());
            complaint.setLatitude(request.getLatitude());
            complaint.setWardNumber(request.getWardNumber());

            Complaint savedComplaint = complaintRepository.save(complaint);

            // Create complaint status
            ComplaintStatus status = new ComplaintStatus();
            status.setComplaint(savedComplaint);
            status.setStatus("In Progress");
            status.setUpdatedBy(assignedOfficer);
            status.setRemarks("Assigned to junior officer of ward " + request.getWardNumber());
            status.setUpdatedAt(LocalDateTime.now());
            status.setEstimatedTime(complaint.getEstimatedTime());
            complaintStatusRepository.save(status);

            // Create initial escalation
            Escalation escalation = new Escalation();
            escalation.setComplaint(savedComplaint);
            escalation.setEscalatedTo(assignedOfficer);
            escalation.setEscalationLevel(1);
            escalation.setReason("Initial assignment to ward officer");
            escalation.setEscalatedAt(LocalDateTime.now());
            escalationRepository.save(escalation);

            // Generate location URL
            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
                    request.getLatitude() + "," + request.getLongitude();

            // Return response
            return new ComplaintDetailsDtoTemplate(
                    assignedOfficer.getName(),
                    assignedOfficer.getMarathiName(),
                    String.valueOf(savedComplaint.getComplaintId()),
                    savedComplaint.getCategory(),
                    savedComplaint.getDescription(),
                    locationUrl,
                    savedComplaint.getImageUrl(),
                    assignedOfficer.getPhoneNumber()
            );

        }



//        public ComplaintDetailsDtoTemplate registerComplaint(Request request) {
//            // Validate required fields
//            System.out.println(request.getWardNumber());
//            if (request.getWardNumber() == null || request.getDepartmentId() == null) {
//                throw new RuntimeException("Ward number and department ID are required");
//            }
//
//            // Find user
//            User user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Find department
//            MunicipalDepartment department = departmentRepository.findById(request.getDepartmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//
//            Officer assignedOfficer = officerRepository
//                    .findFirstByDepartmentAndWardAndRole(
//                            request.getDepartmentId(),
//                            request.getWardNumber()
//                    )
//                    .orElseThrow(() -> new RuntimeException(
//                            "No junior officer found for department ID: " +
//                                    request.getDepartmentId() +
//                                    " and ward number: " +
//                                    request.getWardNumber()
//                    ));
//
//
//            // Create new complaint
//            Complaint complaint = new Complaint();
//            complaint.setCategory(request.getCategory());
//            complaint.setDescription(request.getDescription());
//            complaint.setImageUrl(request.getImageUrl());
//            complaint.setDepartment(department);
//            complaint.setUser(user);
//            complaint.setAssignedOfficer(assignedOfficer);
//            complaint.setStatus("In Progress");
//            complaint.setCreatedAt(LocalDateTime.now());
//            complaint.setEstimatedTime(LocalDateTime.now().plusHours(24)); // Set estimated time to 24 hours from now
//            complaint.setLongitude(request.getLongitude());
//            complaint.setLatitude(request.getLatitude());
//            complaint.setWardNumber(request.getWardNumber());
//
//            Complaint savedComplaint = complaintRepository.save(complaint);
//
//            // Create complaint status
//            ComplaintStatus status = new ComplaintStatus();
//            status.setComplaint(savedComplaint);
//            status.setStatus("In Progress");
//            status.setUpdatedBy(assignedOfficer);
//            status.setRemarks("Assigned to junior officer of ward " + request.getWardNumber());
//            status.setUpdatedAt(LocalDateTime.now());
//            status.setEstimatedTime(complaint.getEstimatedTime());
//            complaintStatusRepository.save(status);
//
//            // Create initial escalation record
//            Escalation escalation = new Escalation();
//            escalation.setComplaint(savedComplaint);
//            escalation.setEscalatedTo(assignedOfficer);
//            escalation.setEscalationLevel(1);
//            escalation.setReason("Initial assignment to ward officer");
//            escalation.setEscalatedAt(LocalDateTime.now());
//            escalationRepository.save(escalation);
//
//            // Create response
//            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" +
//                    request.getLatitude() + "," + request.getLongitude();
//
//            return new ComplaintDetailsDtoTemplate(
//                    assignedOfficer.getName(),
//                    String.valueOf(savedComplaint.getComplaintId()),
//                    String.valueOf(savedComplaint.getDepartment().getDepartmentId()),
//                    savedComplaint.getCategory(),
//                    savedComplaint.getDescription(),
//                    locationUrl,
//                    savedComplaint.getImageUrl(),
//                    assignedOfficer.getPhoneNumber()
//            );
//        }

//        public ComplaintDetailsDtoTemplate  reopenComplaint(Request request) {
//            System.out.println(request.getWardNumber());
//            System.out.println(request);
//            Officer assignedOfficer = officerRepository.findFirstJuniorOfficerByDepartmentAndWard(
//                    request.getDepartmentId(),
//                    request.getWardNumber()
//            ).orElseThrow(() -> new RuntimeException(
//                    "No junior officer found for department ID: " + request.getDepartmentId() +
//                            " and ward number: " + request.getWardNumber()
//            ));
//
//
//            Complaint existingComplaint = complaintRepository.findById(request.getComplaintId())
//                    .orElseThrow(() -> new RuntimeException("Complaint not found"));
//
//            // Calculate estimated time: current time + 40 hours
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime estimatedTime = now.plusHours(40);
//
//
//
//            existingComplaint.setDescription(request.getDescription());
//            existingComplaint.setImageUrl(request.getImageUrl());
//            existingComplaint.setAssignedOfficer(assignedOfficer);
//            existingComplaint.setStatus("Reopened");
//            existingComplaint.setEstimatedTime(estimatedTime);
//            Complaint savedComplaint = complaintRepository.save(existingComplaint);
//
//            ComplaintStatus status = new ComplaintStatus();
//            status.setComplaint(savedComplaint);
//            status.setStatus("Reopened");
//            status.setUpdatedBy(assignedOfficer);
//            status.setRemarks(" Reopened complaint assigned to junior officer");
//            status.setUpdatedAt(LocalDateTime.now());
//            status.setEstimatedTime(estimatedTime);
//            complaintStatusRepository.save(status);
//
//
//            // Prepare response DTO
//
//
//            return null;
//        }

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

            // Get the latest status update time from complaint status history
            String lastUpdatedAt = complaintStatusRepository.findByComplaint(complaint).stream()
                    .max(Comparator.comparing(ComplaintStatus::getUpdatedAt))
                    .map(status -> status.getUpdatedAt().toString())
                    .orElse(complaint.getCreatedAt().toString());

            return new ComplaintDetailsDTO(
                    complaint.getComplaintId(),   // id
                    complaint.getCategory(),      // category
                    complaint.getDescription(),   // description
                    complaint.getStatus(),        // status
                    complaint.getCreatedAt().toString(), // createdAt
                    lastUpdatedAt,               // lastUpdatedAt
                    complaint.getLongitude(),     // longitude
                    complaint.getLatitude(),      // latitude
                    user.getName(),              // userName// phoneNumber
                    user.getWhatsappId(),        // whatsappId
                    complaint.getImageUrl(), // imageUrl
                    complaint.getResolvedImageUrl(),
                    "https://www.google.com/maps/search/?api=1&query=" + complaint.getLatitude() + "," + complaint.getLongitude() // locationUrl
                    ,0,null,null,null
            );
        }

        public List<ComplaintDetailsDTO> getAllComplaints() {
            List<Complaint> complaints = complaintRepository.findAll();

            if (complaints.isEmpty()) {
                throw new RuntimeException("No complaints found");
            }

            return complaints.stream().map(complaint -> {
                User user = complaint.getUser();

                // Get the latest status update time
                String lastUpdatedAt = complaintStatusRepository.findByComplaint(complaint).stream()
                        .max(Comparator.comparing(ComplaintStatus::getUpdatedAt))
                        .map(status -> status.getUpdatedAt().toString())
                        .orElse(complaint.getCreatedAt().toString());

                // Get the latest escalation for this complaint
                Escalation latestEscalation = escalationRepository
                        .findTopByComplaintOrderByEscalatedAtDesc(complaint)
                        .orElse(null);

                // Get escalation level and officer details
                Integer escalationLevel = 0;
                String lastEscalatedOfficerName = "";
                String lastEscalatedOfficerPhone = "";
                String lastEscalatedOfficeerDesignation = ""; // Assuming role is the designation
                if (latestEscalation != null) {
                    escalationLevel = latestEscalation.getEscalationLevel();
                    Officer lastEscalatedOfficer = latestEscalation.getEscalatedTo();
                    if (lastEscalatedOfficer != null) {
                        lastEscalatedOfficerName = lastEscalatedOfficer.getName();
                        lastEscalatedOfficerPhone = lastEscalatedOfficer.getPhoneNumber();
                        lastEscalatedOfficeerDesignation = lastEscalatedOfficer.getDesignation(); // Assuming role is the designation
                    }
                }

                return new ComplaintDetailsDTO(
                        complaint.getComplaintId(),
                        complaint.getCategory(),
                        complaint.getDescription(),
                        complaint.getStatus(),
                        complaint.getCreatedAt().toString(),
                        lastUpdatedAt,
                        complaint.getLongitude(),
                        complaint.getLatitude(),
                        user.getName(),
                        user.getWhatsappId(),
                        complaint.getImageUrl(),
                        complaint.getResolvedImageUrl(),
                        "https://www.google.com/maps/search/?api=1&query=" + complaint.getLatitude() + "," + complaint.getLongitude(),
                        escalationLevel,
                        lastEscalatedOfficerName,
                        lastEscalatedOfficeerDesignation,
                        lastEscalatedOfficerPhone
                );
            }).collect(Collectors.toList());
        }

        public ComplaintEscalationResponseDTO getComplaintEscalationHistory(Long complaintId) {
            Complaint complaint = complaintRepository.findById(complaintId)
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            // Get escalation history
            List<Escalation> escalations = escalationRepository
                    .findByComplaintOrderByEscalatedAtAsc(complaint);

            List<EscalationDTO> escalationHistory = escalations.stream()
                    .map(escalation -> new EscalationDTO(
                            escalation.getEscalationLevel(),
                            escalation.getEscalatedTo().getName(),
                            escalation.getEscalatedTo().getPhoneNumber(),
                            escalation.getEscalatedAt().toString()
                    ))
                    .collect(Collectors.toList());

            // Get resolved date if complaint is resolved
            String resolvedDate = null;
            if ("Resolved".equals(complaint.getStatus())) {
                resolvedDate = complaintStatusRepository.findByComplaint(complaint).stream()
                        .filter(status -> "Resolved".equals(status.getStatus()))
                        .max(Comparator.comparing(ComplaintStatus::getUpdatedAt))
                        .map(status -> status.getUpdatedAt().toString())
                        .orElse(null);
            }

            // Create response
            return new ComplaintEscalationResponseDTO(
                    complaint.getDepartment().getDepartmentName(),
                    complaint.getDescription(),
                    complaint.getWardNumber(),
                    complaint.getUser().getName(),
                    complaint.getUser().getWhatsappId(),
                    resolvedDate,
                    complaint.getImageUrl(),
                    complaint.getResolvedImageUrl(),
                    "https://www.google.com/maps/search/?api=1&query=" + complaint.getLatitude() + "," + complaint.getLongitude(),
                    escalationHistory
            );
        }

        public OfficerDTO assignComplaintToOfficer(Request request) {
            Complaint complaint = complaintRepository.findById(request.getComplaintId())
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            Officer officer;
            if (request.getOfficerId() != null) {
                officer = officerRepository.findById(request.getOfficerId())
                        .orElseThrow(() -> new RuntimeException("Officer not found with ID: " + request.getOfficerId()));
            } else if (request.getPhoneNumber() != null) {
                officer = officerRepository.findByPhoneNumber(request.getPhoneNumber())
                        .orElseThrow(() -> new RuntimeException("Officer not found with phone number: " + request.getPhoneNumber()));
            } else {
                throw new RuntimeException("Either officerId or phone number must be provided");
            }

            // Update main complaint status
            complaint.setStatus(request.getStatus());
            complaint.setAssignedOfficer(officer); // Set assigned officer
            complaintRepository.save(complaint);

            // Save complaint status history
            ComplaintStatus complaintStatus = new ComplaintStatus();
            complaintStatus.setComplaint(complaint);
            complaintStatus.setStatus(request.getStatus());
            complaintStatus.setUpdatedBy(officer);
            complaintStatus.setRemarks(request.getRemarks());
            complaintStatus.setUpdatedAt(LocalDateTime.now());
            complaintStatusRepository.save(complaintStatus);

            // Create initial escalation record
            Escalation escalation = new Escalation();
            escalation.setComplaint(complaint);
            escalation.setEscalatedTo(officer);
            escalation.setEscalationLevel(1);
            escalation.setReason("Initial assignment");
            escalation.setEscalatedAt(LocalDateTime.now());
            escalationRepository.save(escalation);


            return new OfficerDTO(
                    officer.getOfficerId(),
                    officer.getName(),
                    officer.getPhoneNumber(),
                    officer.getEmail(),
                    officer.getRole(),
                   null,
                    null,
                    null
            );
//            (
//                    officer.getOfficerId(),
//                    officer.getName(),
//                    officer.getPhoneNumber(),
//                    officer.getEmail(),
//                    officer.getRole()
////                    officer.getDepartment().getDepartmentName(),
////                    officer.getDepartment().getDepartmentId()
//            );
        }


        public OfficerDTO updateComplaintStatus(Request request) {
            // First find the complaint
            Complaint complaint = complaintRepository.findById(request.getComplaintId())
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            // Find the requesting officer
            Officer requestingOfficer;
            if (request.getOfficerId() != null) {
                requestingOfficer = officerRepository.findById(request.getOfficerId())
                        .orElseThrow(() -> new RuntimeException("Officer not found with ID: " + request.getOfficerId()));
            } else if (request.getPhoneNumber() != null) {
                requestingOfficer = officerRepository.findByPhoneNumber(request.getPhoneNumber())
                        .orElseThrow(() -> new RuntimeException("Officer not found with phone number: " + request.getPhoneNumber()));
            } else {
                throw new RuntimeException("Either officerId or phone number must be provided");
            }
            System.out.println(complaint.getAssignedOfficer().getPhoneNumber());
            System.out.println(request);
            System.out.println(requestingOfficer.getOfficerId());
            System.out.println(complaint.getAssignedOfficer().getOfficerId());
            // Check if the requesting officer is the assigned officer
            if (!requestingOfficer.getOfficerId().equals(complaint.getAssignedOfficer().getOfficerId())) {
                throw new RuntimeException("Unauthorized: Only the assigned officer can update this complaint's status");
            }

            // Update main complaint status
            complaint.setStatus(request.getStatus());
            complaint.setResolvedImageUrl(request.getImageUrl());
            complaintRepository.save(complaint);

            // Save complaint status history
            ComplaintStatus complaintStatus = new ComplaintStatus();
            complaintStatus.setComplaint(complaint);
            complaintStatus.setStatus(request.getStatus());
            complaintStatus.setUpdatedBy(requestingOfficer);
            complaintStatus.setRemarks(request.getRemarks());
            complaintStatus.setUpdatedAt(LocalDateTime.now());
            complaintStatusRepository.save(complaintStatus);

            return new OfficerDTO(
                    requestingOfficer.getOfficerId(),
                    requestingOfficer.getName(),
                    requestingOfficer.getPhoneNumber(),
                    requestingOfficer.getEmail(),
                    requestingOfficer.getRole(),
                    null,
                    null,
                    null
            );
        }

//        public OfficerDTO updateComplaintStatus(Request request) {
//            Complaint complaint = complaintRepository.findById(request.getComplaintId())
//                    .orElseThrow(() -> new RuntimeException("Complaint not found"));
//
//            Officer officer;
//            if (request.getOfficerId() != null) {
//                // If officerId is present, find officer by ID
//                officer = officerRepository.findById(request.getOfficerId())
//                        .orElseThrow(() -> new RuntimeException("Officer not found with ID: " + request.getOfficerId()));
//            } else if (request.getPhoneNumber() != null) {
//                // If no officerId but phone number is present, find officer by phone
//                officer = officerRepository.findByPhoneNumber(request.getPhoneNumber())
//                        .orElseThrow(() -> new RuntimeException("Officer not found with phone number: " + request.getPhoneNumber()));
//            } else {
//                throw new RuntimeException("Either officerId or phone number must be provided");
//            }// Check if the requesting officer is the assigned officer
//            if (!requestingOfficer.getOfficerId().equals(complaint.getAssignedOfficer().getOfficerId())) {
//                throw new RuntimeException("Unauthorized: Only the assigned officer can update this complaint's status");
//            }
//
//
//            // Update main complaint status
//            complaint.setStatus(request.getStatus());
//            complaintRepository.save(complaint);
//
//            // Save complaint status history
//            ComplaintStatus complaintStatus = new ComplaintStatus();
//            complaintStatus.setComplaint(complaint);
//            complaintStatus.setStatus(request.getStatus());
//            complaintStatus.setUpdatedBy(officer);
//            complaintStatus.setRemarks(request.getRemarks());
//            complaintStatus.setUpdatedAt(LocalDateTime.now());
//
//            complaintStatusRepository.save(complaintStatus);
//
//            return new OfficerDTO(
//                    officer.getOfficerId(),
//                    officer.getName(),
//                    officer.getPhoneNumber(),
//                    officer.getEmail(),
//                    officer.getRole(),
//                    null,
//                    null,
//                    null
////                    officer.getAssignedZone(),
////                    officer.getDepartment().getDepartmentName(),
////                    officer.getDepartment().getDepartmentId()
//            );
//        }

//        private void sendWhatsAppMessage(Complaint complaint) {
//            String apiUrl = "https://api.auurumdigital.com/v2/wamessage/sendMessage";
//            String from = "919175781771"; // your registered number
//            String to  = complaint.getAssignedOfficer().getPhoneNumber(); // assuming this is in correct format
//            System.out.println(to);
//            List<String> placeholders = List.of(
//                    complaint.getAssignedOfficer().getName(),
//                    String.valueOf(complaint.getComplaintId()),
//                    complaint.getCategory(),
//                    complaint.getDescription(),
//                    "https://www.google.com/maps/search/?api=1&query=" + complaint.getLatitude() + "," + complaint.getLongitude(),
//                    complaint.getImageUrl() != null ? complaint.getImageUrl() : "No image"
//            );
//
//            WhatsAppMessageRequest.Message message = new WhatsAppMessageRequest.Message("807889", placeholders);
//            WhatsAppMessageRequest request = new WhatsAppMessageRequest(from, to, "template", message);
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // ✅ Add apikey header
//            headers.set("apikey", "d9f93ae9-fc1a-11ef-ad4f-92672d2d0c2d");
//
//            HttpEntity<WhatsAppMessageRequest> httpRequest = new HttpEntity<>(request, headers);
//
//            try {
//                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpRequest, String.class);
//                System.out.println("WhatsApp message sent: " + response.getBody());
//            } catch (Exception e) {
//                System.err.println("Failed to send WhatsApp message: " + e.getMessage());
//            }
//        }


        public List<PredefinedComplaintDTO>  getComplaintsByDepartmentId(Long departmentId) {
            List<PredefinedComplaint> complaints = predefinedComplaintRepository.findByDepartment_DepartmentId(departmentId);

            if (complaints.isEmpty()) {
                throw new RuntimeException("No predefined complaints found for department ID: " + departmentId);
            }

            return complaints.stream()
                    .map(c -> new PredefinedComplaintDTO(c.getId(), c.getName(), c.getDescription(),c.getMarathiName(),c.getMarathiDescription()))
                    .collect(Collectors.toList());
        }

        public void updateEstimatedTime(Request request) {
            // Get complaint
            Complaint complaint = complaintRepository.findById(request.getComplaintId())
                    .orElseThrow(() -> new RuntimeException("Complaint not found"));

            // Get officer
            Officer requestingOfficer = officerRepository.findFirstByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Officer not found with given phone number"));

            // Check if requesting officer is the assigned officer
            if (!requestingOfficer.getOfficerId().equals(complaint.getAssignedOfficer().getOfficerId())) {
                throw new RuntimeException("Unauthorized: Only the assigned officer can update estimated time");
            }

            // Parse date and set time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate parsedDate = LocalDate.parse(request.getEstimatedTimeStr(), formatter);
            LocalDateTime estimatedTime = parsedDate.atStartOfDay();

            // Update complaint
            complaint.setEstimatedTime(estimatedTime);
            complaint.setStatus("Pending");
            complaintRepository.save(complaint);

            // Create and save complaint status
            ComplaintStatus status = new ComplaintStatus();
            status.setComplaint(complaint);
            status.setStatus("Pending");
            status.setUpdatedBy(requestingOfficer);
            status.setRemarks(request.getRemarks());
            status.setUpdatedAt(LocalDateTime.now());
            status.setEstimatedTime(estimatedTime);
            complaintStatusRepository.save(status);
        }


    }
