package com.ashish.fitness.feedback;

import com.ashish.fitness.equipment.Equipment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return  Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .equipment(Equipment.builder()
                        .id(request.equipmentId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(),id))
                .build();
    }
}
