package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.PredefinedComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredefinedComplaintRepository extends JpaRepository<PredefinedComplaint, Long> {
    List<PredefinedComplaint> findByDepartment_DepartmentId(Long departmentId);
}

