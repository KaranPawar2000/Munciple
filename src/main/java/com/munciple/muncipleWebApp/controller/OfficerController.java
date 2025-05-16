package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.service.OfficerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
