package com.ashish.fitness.feedback;

import com.ashish.fitness.common.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbacksByEquipments(Integer equipmentId, int page, int size, Authentication connectedUser);
}
