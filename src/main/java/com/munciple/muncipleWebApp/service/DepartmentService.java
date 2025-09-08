package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.DepartmentDTO;
import com.munciple.muncipleWebApp.entity.MunicipalDepartment;
import com.munciple.muncipleWebApp.repo.MunicipalDepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DepartmentService {

    private final MunicipalDepartmentRepository departmentRepository;

    public DepartmentService(MunicipalDepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<MunicipalDepartment> departments = departmentRepository.findAll();
        return departments.stream().map(dept -> new DepartmentDTO(
                dept.getDepartmentId(),
                dept.getDepartmentName(),
                dept.getMarathidepartmentName(),
                dept.getCity(),
                dept.isStatus()
        )).collect(Collectors.toList());
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        MunicipalDepartment department = new MunicipalDepartment();
        department.setDepartmentName(departmentDTO.getName());
        department.setCity(departmentDTO.getCity());
        department.setMarathidepartmentName(departmentDTO.getMarathiName());
        department.setStatus(departmentDTO.isStatus());

        MunicipalDepartment saved = departmentRepository.save(department);

        DepartmentDTO responseDTO = new DepartmentDTO();
        responseDTO.setId(saved.getDepartmentId());
        responseDTO.setName(saved.getDepartmentName());
        responseDTO.setCity(saved.getCity());
        responseDTO.setMarathiName(saved.getMarathidepartmentName());
        responseDTO.setStatus(saved.isStatus());

        return responseDTO;
    }

    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        MunicipalDepartment department = departmentRepository.findById(departmentDTO.getId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentDTO.getId()));

        department.setDepartmentName(departmentDTO.getName());
        department.setCity(departmentDTO.getCity());
        department.setMarathidepartmentName(departmentDTO.getMarathiName());
        department.setStatus(departmentDTO.isStatus());

        MunicipalDepartment updated = departmentRepository.save(department);

        return new DepartmentDTO(
                updated.getDepartmentId(),
                updated.getDepartmentName(),
                updated.getMarathidepartmentName(),
                updated.getCity(),
                updated.isStatus()
        );
    }


}
