package com.munciple.muncipleWebApp.controller;

import com.munciple.muncipleWebApp.dto.FeedbackDTO;
import com.munciple.muncipleWebApp.entity.Feedback;
import com.munciple.muncipleWebApp.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public ResponseEntity<String> addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        String savedFeedback = feedbackService.addFeedback(feedbackDTO);
        return ResponseEntity.ok(savedFeedback);
    }
}
