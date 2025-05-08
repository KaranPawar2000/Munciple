package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.repo.OfficerRepository;
import org.springframework.stereotype.Service;


@Service
public class OfficerService {
    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public OfficerDTO getOfficerByDepartmentAndRole(Long departmentId, String role) {
        return officerRepository.findFirstByDepartment_DepartmentIdAndRole(departmentId, role)
                .map(officer -> new OfficerDTO(officer.getOfficerId(), officer.getName(), officer.getPhoneNumber(), officer.getEmail(), officer.getRole(), officer.getAssignedZone()))
                .orElse(null);
    }
}
