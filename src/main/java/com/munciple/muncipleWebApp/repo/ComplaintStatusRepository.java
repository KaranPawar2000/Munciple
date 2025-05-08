package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintStatusRepository extends JpaRepository<ComplaintStatus, Long> {
}
