package com.code.auditor.controllers;

import com.code.auditor.dtos.FeedbackCreationDTO;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.repositories.FeedbackRepository;
import com.code.auditor.services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackService feedbackService, FeedbackRepository feedbackRepository) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
    }

    @PostMapping()
    public ResponseEntity<Object> addFeedback(@RequestBody FeedbackCreationDTO feedbackCreationDTO){
        feedbackService.createFeedback(feedbackCreationDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Коментарът беше създаден успешно!"));
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Object> deleteFeedback(@PathVariable Long feedbackId){
        feedbackRepository.deleteById(feedbackId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Обратната връзка беше изтрита успешно."));
    }
}
