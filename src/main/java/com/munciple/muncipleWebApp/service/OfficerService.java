package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.OfficerDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.entity.MunicipalDepartment;
import com.munciple.muncipleWebApp.entity.Officer;
import com.munciple.muncipleWebApp.repo.MunicipalDepartmentRepository;
import com.munciple.muncipleWebApp.repo.OfficerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfficerService {
    private final OfficerRepository officerRepository;
    private final MunicipalDepartmentRepository municipalDepartmentRepository;

    public OfficerService(OfficerRepository officerRepository, MunicipalDepartmentRepository municipalDepartmentRepository) {
        this.officerRepository = officerRepository;
        this.municipalDepartmentRepository = municipalDepartmentRepository;
    }

    public OfficerDTO getOfficerByDepartmentAndRole(Long departmentId, String role) {
        return  officerRepository.findFirstByDepartments_DepartmentIdAndRole(departmentId, role)
                .map(officer -> new OfficerDTO(
                        officer.getOfficerId(),
                        officer.getName(),
                        officer.getPhoneNumber(),
                        officer.getEmail(),
                        officer.getRole(),
                        null,
                        null,
                        null
                ))
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
            ;

            return info;
        }).collect(Collectors.toList());

        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Officers fetched successfully");
        response.setOfficers(officerInfoList);

        return response;
    }

    public OfficerDTO updateOfficer(OfficerDTO dto) {
        Officer officer = officerRepository.findById(dto.getOfficerId())
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + dto.getOfficerId()));

        if (dto.getName() != null) officer.setName(dto.getName());
        if (dto.getPhoneNumber() != null) officer.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) officer.setEmail(dto.getEmail());
        if (dto.getRole() != null) officer.setRole(dto.getRole());


        Officer updated = officerRepository.save(officer);

        return new OfficerDTO(
                updated.getOfficerId(),
                updated.getName(),
                updated.getPhoneNumber(),
                updated.getEmail(),
                updated.getRole(),
                null,
                null,
                 null
        );
    }

    public boolean deleteOfficer(Long officerId) {
        if (officerRepository.existsById(officerId)) {
            officerRepository.deleteById(officerId);
            return true;
        } else {
            return false;
        }
    }

    public OfficerDTO addOfficer(OfficerDTO dto) {
        Officer officer = new Officer();
        officer.setName(dto.getName());
        officer.setPhoneNumber(dto.getPhoneNumber());
        officer.setEmail(dto.getEmail());
        officer.setRole(dto.getRole());

        MunicipalDepartment department = municipalDepartmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));



        Officer saved = officerRepository.save(officer);

        // Build DTO to return
        OfficerDTO response = new OfficerDTO(
                saved.getOfficerId(),
                saved.getName(),
                saved.getPhoneNumber(),
                saved.getEmail(),
                saved.getRole(),
                null,
                department.getDepartmentName(),
                department.getDepartmentId()
        );

        return response;
    }

    public List<OfficerDTO> getOfficersByDepartment(Long departmentId) {
        List<Officer> officers = officerRepository.findByDepartment_DepartmentId(departmentId);

        if (officers.isEmpty()) {
            throw new RuntimeException("No officers found for department ID: " + departmentId);
        }

        return officers.stream()
                .map(officer -> new OfficerDTO(
                        officer.getOfficerId(),
                        officer.getName(),
                        officer.getPhoneNumber(),
                        officer.getEmail(),
                        officer.getRole(),
                        null,
                        null,
                        null
                ))
                .collect(Collectors.toList());
    }

}
