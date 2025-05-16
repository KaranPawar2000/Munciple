package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.ComplaintDetailsDTO;
import com.munciple.muncipleWebApp.dto.ComplaintDetailsDtoTemplate;
import com.munciple.muncipleWebApp.dto.PredefinedComplaintDTO;
import com.munciple.muncipleWebApp.dto.Request;
import com.munciple.muncipleWebApp.entity.Complaint;
import com.munciple.muncipleWebApp.entity.PredefinedComplaint;
import com.munciple.muncipleWebApp.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/register")
    public ResponseEntity<?> registerComplaint(@RequestBody Request request) {
        try {
            ComplaintDetailsDtoTemplate dto = complaintService.registerComplaint(request);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{complaintId}")
    public ResponseEntity<?> getComplaintDetails(@PathVariable Long complaintId) {
        try {
            ComplaintDetailsDTO dto = complaintService.getComplaintDetailsById(complaintId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update-status")
    public ResponseEntity<?> updateComplaintStatus(@RequestBody Request request) {
        System.out.println("API Hit");
        try {
            complaintService.updateComplaintStatus(request);
            return ResponseEntity.ok(Map.of("message", "Complaint status updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping("/predefined/{departmentId}")
    public ResponseEntity<?> getPredefinedComplaints(@PathVariable Long departmentId) {
        List<PredefinedComplaintDTO> complaints = complaintService.getComplaintsByDepartmentId(departmentId);
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/assign")
    public ResponseEntity<?> assignComplaintToOfficer(@RequestBody Request request) {
        try {
            System.out.println("request.getComplaintId(),request.getPhoneNumber()");
            complaintService.assignComplaintToOfficer(request.getComplaintId(), request.getPhoneNumber());
            return ResponseEntity.ok(Map.of("message", "Complaint assigned Successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getUnresolvedComplaints() {

        try {
            List<ComplaintDetailsDTO> complaints = complaintService.getAllComplaints();
            return  ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(complaints);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        }
    }


    @PutMapping("/update-estimated-time")
    public ResponseEntity<?> updateEstimatedTime(@RequestBody Request request) {
        try {
            complaintService.updateEstimatedTime(request);
            return ResponseEntity.ok(Map.of("message", "Estimated time updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



}