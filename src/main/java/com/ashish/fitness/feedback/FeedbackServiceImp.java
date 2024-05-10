package com.ashish.fitness.feedback;

import com.ashish.fitness.common.PageResponse;
import com.ashish.fitness.equipment.Equipment;
import com.ashish.fitness.equipment.EquipmentRepository;
import com.ashish.fitness.exception.OperationNotPermittedException;
import com.ashish.fitness.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FeedbackServiceImp implements FeedbackService{
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Override
    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(request.equipmentId())
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+request.equipmentId()));
        if(equipment.isArchived() || !equipment.isSharable()){
            throw  new OperationNotPermittedException("You cannot give a feedback for an archived or not sharable equipment");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
            throw new OperationNotPermittedException("You can't give a feedback to your own book ");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbacksByEquipments(Integer equipmentId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page,size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllByEquipmentId(equipmentId,pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f-> feedbackMapper.toFeedbackResponse(f,user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
