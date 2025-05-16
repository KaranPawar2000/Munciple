package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.entity.Officer;
import com.munciple.muncipleWebApp.repo.OfficerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public Response getAllOfficers() {
        List<Officer> officers = officerRepository.findAll();

        List<Response.OfficerInfo> officerInfoList = officers.stream().map(officer -> {
            Response.OfficerInfo info = new Response.OfficerInfo();
            info.setOfficerId(officer.getOfficerId());
            info.setName(officer.getName());
            info.setPhoneNumber(officer.getPhoneNumber());
            info.setEmail(officer.getEmail());
            info.setRole(officer.getRole());
            info.setAssignedZone(officer.getAssignedZone());
            info.setCreatedAt(officer.getCreatedAt());
            info.setDepartmentName(
                    officer.getDepartment() != null ? officer.getDepartment().getDepartmentName() : null
            );
            return info;
        }).collect(Collectors.toList());

        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Officers fetched successfully");
        response.setOfficers(officerInfoList);

        return response;
    }
}
