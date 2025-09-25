package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.MunicipalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipalDepartmentRepository extends JpaRepository<MunicipalDepartment, Long> {

    List<MunicipalDepartment> findByStatusTrue();
}
