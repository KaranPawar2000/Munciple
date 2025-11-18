package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.GetAllOfficerDTO;
import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.service.OfficerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<GetAllOfficerDTO>> getAllOfficers() {

        List<GetAllOfficerDTO> response = officerService.getAllOfficers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{officerId}")
    public ResponseEntity<GetAllOfficerDTO> getOfficerById(@PathVariable Long officerId) {
        try {
            GetAllOfficerDTO officer = officerService.getOfficerPersonalDetailsById(officerId);
            return ResponseEntity.ok(officer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/update/{officerId}")
    public ResponseEntity<GetAllOfficerDTO> updateOfficer(
            @PathVariable Long officerId,
            @RequestBody GetAllOfficerDTO dto) {
        try {
            GetAllOfficerDTO updatedOfficer = officerService.updateOfficer(officerId, dto);
            return ResponseEntity.ok(updatedOfficer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




//    @PutMapping("/update")
//    public ResponseEntity<OfficerDTO> updateOfficer(@RequestBody OfficerDTO dto) {
//        OfficerDTO updatedOfficer = officerService.updateOfficer(dto);
//        return ResponseEntity.ok(updatedOfficer);
//    }

//    @DeleteMapping("/delete/{officerId}")
//    public ResponseEntity<String> deleteOfficer(@PathVariable Long officerId) {
//        boolean isDeleted = officerService.deleteOfficer(officerId);
//        if (isDeleted) {
//            return ResponseEntity.ok("Officer deleted successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Officer not found.");
//        }
//    }

    @PostMapping("/add")
    public ResponseEntity<OfficerDTO> addOfficer(@RequestBody OfficerDTO dto) {
        OfficerDTO createdOfficer = officerService.addOfficer(dto);
        return new ResponseEntity<>(createdOfficer, HttpStatus.CREATED);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getOfficersByDepartment(@PathVariable Long departmentId) {
        try {
            List<OfficerDTO> officers = officerService.getOfficersByDepartment(departmentId);
            return ResponseEntity.ok(officers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
