package com.ashish.fitness.equipment;

import com.ashish.fitness.file.FileUtils;
import com.ashish.fitness.history.EquipmentTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class EquipmentMapper {
    public Equipment toEquipment(EquipmentRequest request) {
        return Equipment.builder()
                .id(request.id())
                .equipmentName(request.equipmentName())
                .description(request.description())
                .isbn(request.isbn())
                .archived(false)
                .sharable(request.sharable())
                .build();
    }

    public EquipmentResponse toEquipmentResponse(Equipment equipment) {
        return EquipmentResponse.builder()
                .id(equipment.getId())
                .equipmentName(equipment.getEquipmentName())
                .isbn(equipment.getIsbn())
                .description(equipment.getDescription())
                .rate(equipment.getRate())
                .archived(equipment.isArchived())
                .sharable(equipment.isSharable())
                .owner(equipment.getOwner().fullName())
                .image(equipment.getImage())
                .build();
    }

    public BorrowedEquipmentResponse toBorrowedEquipmentResponse(EquipmentTransactionHistory history) {
        return  BorrowedEquipmentResponse.builder()
                .id(history.getEquipment().getId())
                .equipmentName(history.getEquipment().getEquipmentName())
                .isbn(history.getEquipment().getIsbn())
                .rate(history.getEquipment().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
