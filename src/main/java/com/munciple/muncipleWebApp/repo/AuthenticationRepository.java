package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    Optional<Authentication> findByUsername(String username);
}
