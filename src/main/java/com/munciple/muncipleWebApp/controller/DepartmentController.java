package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.DepartmentDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/get/departments")
    public ResponseEntity<Response> getDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Departments fetched successfully");
        response.setDepartments(departments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
