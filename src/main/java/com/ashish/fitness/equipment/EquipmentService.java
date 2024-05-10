package com.ashish.fitness.equipment;

import com.ashish.fitness.common.PageResponse;
import org.springframework.security.core.Authentication;

public interface EquipmentService {
    Integer save(EquipmentRequest request, Authentication connectedUser);

    EquipmentResponse findById(Integer equipmentId);

    PageResponse<EquipmentResponse> findAllEquipments(int page, int size, Authentication connectedUser);

    PageResponse<EquipmentResponse> findAllEquipmentsByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedEquipmentResponse> findAllBorrowedEquipments(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedEquipmentResponse> findAllReturnedEquipments(int page, int size, Authentication connectedUser);

    Integer updateShareableStatus(Integer equipmentId, Authentication connectedUser);

    Integer updateArchivedStatus(Integer equipmentId, Authentication connectedUser);

    Integer borrowEquipment(Integer equipmentId, Authentication connectedUser);

    Integer returnBorrowedBook(Integer equipmentId, Authentication connectedUser);
}
