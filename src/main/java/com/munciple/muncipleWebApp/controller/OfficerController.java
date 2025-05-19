package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.service.OfficerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/officer")
public class OfficerController {
    private final OfficerService officerService;

    public OfficerController( OfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping("/get/officer/{departmentId}/{role}")
    public ResponseEntity<OfficerDTO> getOfficerByDepartmentAndRole(@PathVariable Long departmentId, @PathVariable String role) {
        OfficerDTO officer = officerService.getOfficerByDepartmentAndRole(departmentId, role);
        return officer != null ? new ResponseEntity<>(officer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<Response> getAllOfficers() {
        Response response = officerService.getAllOfficers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<OfficerDTO> updateOfficer(@RequestBody OfficerDTO dto) {
        OfficerDTO updatedOfficer = officerService.updateOfficer(dto);
        return ResponseEntity.ok(updatedOfficer);
    }

    @DeleteMapping("/delete/{officerId}")
    public ResponseEntity<String> deleteOfficer(@PathVariable Long officerId) {
        boolean isDeleted = officerService.deleteOfficer(officerId);
        if (isDeleted) {
            return ResponseEntity.ok("Officer deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Officer not found.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<OfficerDTO> addOfficer(@RequestBody OfficerDTO dto) {
        OfficerDTO createdOfficer = officerService.addOfficer(dto);
        return new ResponseEntity<>(createdOfficer, HttpStatus.CREATED);
    }

}
