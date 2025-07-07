package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.MunicipalDepartment;
import com.munciple.muncipleWebApp.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
    Optional<Officer> findFirstByDepartment_DepartmentIdAndRole(Long departmentId, String role);

        @Query("SELECT o FROM Officer o JOIN OfficerAssignment oa ON o.officerId = oa.officer.officerId " +
                "WHERE oa.wardNumber = :wardNumber AND oa.department.departmentId = :departmentId " +
                "AND oa.role = '1'")
        Optional<Officer> findJuniorOfficerByWardAndDepartment(
                @Param("wardNumber") String wardNumber,
                @Param("departmentId") Long departmentId
        );

    Optional<Officer> findByRole(String role);

    Optional<Officer> findByDepartmentAndRole(MunicipalDepartment department, String role);

    Optional<Officer> findByPhoneNumber(String phone_number);

    List<Officer> findByDepartment_DepartmentId(Long departmentId);

    Optional<Officer> findByDepartment_DepartmentIdAndRole(Long departmentId, String role);
}


