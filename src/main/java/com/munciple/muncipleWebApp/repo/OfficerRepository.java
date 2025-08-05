package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.MunicipalDepartment;
import com.munciple.muncipleWebApp.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {

//    @Query("SELECT DISTINCT o FROM Officer o JOIN o.departments od " +
//            "WHERE od.department.departmentId = :departmentId " +
//            "AND od.wardNumber = :wardNumber " +
//            "AND o.role = '1' " +
//            "ORDER BY o.officerId ASC " +
//            "LIMIT 1")
//    Optional<Officer> findFirstByDepartmentAndWardAndRole(
//            @Param("departmentId") Long departmentId,
//            @Param("wardNumber") String wardNumber
//    );

    @Query("SELECT DISTINCT o FROM Officer o JOIN o.departments od " +
            "WHERE od.department.departmentId = :departmentId " +
            "AND od.wardNumber = :wardNumber " +
            "AND o.role = :role " +
            "ORDER BY o.officerId ASC " +
            "LIMIT 1")
    Optional<Officer> findFirstByDepartmentAndWardAndRole(
            @Param("departmentId") Long departmentId,
            @Param("wardNumber") String wardNumber,
            @Param("role") String role
    );

    @Query("SELECT o FROM Officer o JOIN o.departments od " +
            "WHERE od.department.departmentId = :departmentId " +
            "AND o.role = :role " +
            "ORDER BY o.officerId ASC " +
            "LIMIT 1")
    Optional<Officer> findFirstByDepartments_DepartmentIdAndRole(
            @Param("departmentId") Long departmentId,
            @Param("role") String role
    );


    @Query("SELECT o FROM Officer o JOIN o.departments od " +
            "WHERE od.department.departmentId = :departmentId " +
            "AND od.wardNumber = :wardNumber " +
            "AND o.role = '1' " +
            "ORDER BY o.officerId ASC " +
            "LIMIT 1")
    Optional<Officer> findJuniorOfficerByDepartmentAndWard(
            @Param("departmentId") Long departmentId,
            @Param("wardNumber") String wardNumber
    );

    @Query("SELECT o FROM Officer o JOIN o.departments od WHERE od.department = :department AND o.role = :role")
    Optional<Officer> findByDepartmentAndRole(
            @Param("department") MunicipalDepartment department,
            @Param("role") String role
    );

    Optional<Officer> findByPhoneNumber(String phoneNumber);


    @Query("SELECT o FROM Officer o WHERE o.phoneNumber = :phoneNumber ORDER BY o.officerId ASC LIMIT 1")
    Optional<Officer> findFirstByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT DISTINCT o FROM Officer o JOIN o.departments od WHERE od.department.departmentId = :departmentId")
    List<Officer> findByDepartment_DepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT o FROM Officer o JOIN o.departments od " +
            "WHERE od.department.departmentId = :departmentId AND o.role = '1' " +
            "AND od.wardNumber = :wardNumber " +
            "ORDER BY o.officerId ASC")
    Optional<Officer> findJuniorOfficerByWardAndDepartment(
            @Param("wardNumber") String wardNumber,
            @Param("departmentId") Long departmentId
    );
}

//public interface OfficerRepository extends JpaRepository<Officer, Long> {
//
//
//
//    @Query("SELECT o FROM Officer o JOIN OfficerAssignment oa ON o.officerId = oa.officer.officerId " +
//            "WHERE oa.wardNumber = :wardNumber AND oa.department.departmentId = :departmentId " +
//            "AND oa.role = '1' ORDER BY o.officerId ASC LIMIT 1")
//    Optional<Officer> findJuniorOfficerByWardAndDepartment(
//            @Param("wardNumber") String wardNumber,
//            @Param("departmentId") Long departmentId
//    );
//
//    Optional<Officer> findByRole(String role);
//
//    @Query("SELECT o FROM Officer o JOIN o.departments od WHERE od.department = :department AND o.role = :role")
//    Optional<Officer> findByDepartmentAndRole(
//            @Param("department") MunicipalDepartment department,
//            @Param("role") String role
//    );
//    Optional<Officer> findByPhoneNumber(String phone_number);
//
//    List<Officer> findByDepartment_DepartmentId(Long departmentId);
//
//    Optional<Officer> findByDepartment_DepartmentIdAndRole(Long departmentId, String role);
//}


