package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.DepartmentDTO;
import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/get/active/departments")
    public ResponseEntity<Response> getActiveDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllActiveDepartments();
        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Departments fetched successfully");
        response.setDepartments(departments);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/add")
    public ResponseEntity<Response> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Department added successfully");
        response.setDepartments(List.of(createdDepartment));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(departmentDTO);

        Response response = new Response();
        response.setStatus("success");
        response.setMessage("Department updated successfully");
        response.setDepartments(List.of(updatedDepartment));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
