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
                dept.getCity()
        )).collect(Collectors.toList());
    }

}
