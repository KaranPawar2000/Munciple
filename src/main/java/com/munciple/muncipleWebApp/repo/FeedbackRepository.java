package com.munciple.muncipleWebApp.repo;

import com.munciple.muncipleWebApp.entity.Feedback;
import com.munciple.muncipleWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


}

