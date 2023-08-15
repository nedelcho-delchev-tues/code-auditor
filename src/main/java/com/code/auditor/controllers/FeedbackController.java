package com.code.auditor.controllers;

import com.code.auditor.dtos.FeedbackCreationDTO;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping()
    public ResponseEntity<Object> addFeedback(@RequestBody FeedbackCreationDTO feedbackCreationDTO){
        feedbackService.createFeedback(feedbackCreationDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Коментарът беше създаден успешно!"));
    }
}
