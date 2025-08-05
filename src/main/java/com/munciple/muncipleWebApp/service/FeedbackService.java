package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.FeedbackDTO;
import com.munciple.muncipleWebApp.entity.Complaint;
import com.munciple.muncipleWebApp.entity.Feedback;
import com.munciple.muncipleWebApp.entity.User;

import com.munciple.muncipleWebApp.repo.ComplaintRepository;
import com.munciple.muncipleWebApp.repo.FeedbackRepository;
import com.munciple.muncipleWebApp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    public String addFeedback(FeedbackDTO feedbackDTO) {
        Complaint complaint = complaintRepository.findById(feedbackDTO.getComplaintId())
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        User feedbackUser = userRepository.findByWhatsappId(feedbackDTO.getWhatsappId())
                .orElseThrow(() -> new RuntimeException("User not found with given whatsappId"));

        // Ensure only complaint owner can give feedback
        String complaintOwnerWhatsappId = complaint.getUser().getWhatsappId();
        if (!complaintOwnerWhatsappId.equals(feedbackUser.getWhatsappId())) {
            throw new RuntimeException("Unauthorized: Only the complaint owner can submit feedback.");
        }

        Feedback feedback = new Feedback();
        feedback.setComplaint(complaint);
        feedback.setUser(feedbackUser);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComments(feedbackDTO.getComments());

        feedbackRepository.save(feedback);

        return "Feedback sent successfully";
    }


}
