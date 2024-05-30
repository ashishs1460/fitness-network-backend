package com.ashish.fitness.equipment;

import com.ashish.fitness.common.PageResponse;
import com.ashish.fitness.exception.OperationNotPermittedException;
import com.ashish.fitness.file.FileStorageService;
import com.ashish.fitness.history.EquipmentTransactionHistory;
import com.ashish.fitness.history.EquipmentTransactionHistoryRepository;
import com.ashish.fitness.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class EquipmentServiceImp implements EquipmentService{
    @Autowired
    private  EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentTransactionHistoryRepository historyRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Override
    public Integer save(EquipmentRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Equipment equipment = equipmentMapper.toEquipment(request);
        equipment.setOwner(user);
        return equipmentRepository.save(equipment).getId();
    }

    @Override
    public EquipmentResponse findById(Integer equipmentId) {
        return  equipmentRepository.findById(equipmentId)
                .map(equipmentMapper::toEquipmentResponse)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with ID :: "+equipmentId));
    }

    @Override
    public PageResponse<EquipmentResponse> findAllEquipments(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable  pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Equipment> equipments = equipmentRepository.findAllDisplayableEquipments(pageable,user.getId());
        List<EquipmentResponse> equipmentResponses = equipments.stream()
                .map(equipmentMapper::toEquipmentResponse)
                .toList();
        return  new PageResponse<>(
                equipmentResponses,
                equipments.getNumber(),
                equipments.getSize(),
                equipments.getTotalElements(),
                equipments.getTotalPages(),
                equipments.isFirst(),
                equipments.isLast()
        );
    }

    @Override
    public PageResponse<EquipmentResponse> findAllEquipmentsByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable  pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Equipment> equipments = equipmentRepository.findAll(EquipmentSpecification.withOwnerId(user.getId()), pageable);
        List<EquipmentResponse> equipmentResponses = equipments.stream()
                .map(equipmentMapper::toEquipmentResponse)
                .toList();
        return  new PageResponse<>(
                equipmentResponses,
                equipments.getNumber(),
                equipments.getSize(),
                equipments.getTotalElements(),
                equipments.getTotalPages(),
                equipments.isFirst(),
                equipments.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedEquipmentResponse> findAllBorrowedEquipments(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<EquipmentTransactionHistory> allBorrowedEquipments = historyRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedEquipmentResponse>  equipmentResponse = allBorrowedEquipments.stream()
                .map(equipmentMapper::toBorrowedEquipmentResponse)
                .toList();
        return  new PageResponse<>(
                equipmentResponse,
                allBorrowedEquipments.getNumber(),
                allBorrowedEquipments.getSize(),
                allBorrowedEquipments.getTotalElements(),
                allBorrowedEquipments.getTotalPages(),
                allBorrowedEquipments.isFirst(),
                allBorrowedEquipments.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedEquipmentResponse> findAllReturnedEquipments(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable  pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<EquipmentTransactionHistory> allBorrowedEquipments = historyRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedEquipmentResponse>  equipmentResponse = allBorrowedEquipments.stream()
                .map(equipmentMapper::toBorrowedEquipmentResponse)
                .toList();
        return  new PageResponse<>(
                equipmentResponse,
                allBorrowedEquipments.getNumber(),
                allBorrowedEquipments.getSize(),
                allBorrowedEquipments.getTotalElements(),
                allBorrowedEquipments.getTotalPages(),
                allBorrowedEquipments.isFirst(),
                allBorrowedEquipments.isLast()
        );
    }

    @Override
    public Integer updateShareableStatus(Integer equipmentId, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        User user = ((User) connectedUser.getPrincipal());
//        if(Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
//            throw new OperationNotPermittedException("You can't update others equipment shareable status");
//        }
        equipment.setSharable(!equipment.isSharable());
        equipmentRepository.save(equipment);
        return equipmentId;
    }

    @Override
    public Integer updateArchivedStatus(Integer equipmentId, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        User user = ((User) connectedUser.getPrincipal());
//        if(!Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
//            throw new OperationNotPermittedException("You can't update others equipment archived status");
//        }
        equipment.setArchived(!equipment.isArchived());
        equipmentRepository.save(equipment);
        return equipmentId;
    }

    @Override
    public Integer borrowEquipment(Integer equipmentId, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        if(equipment.isArchived() || !equipment.isSharable()){
            throw  new OperationNotPermittedException("The requested equipment is cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
            throw new OperationNotPermittedException("You can't borrow your own equipments");
        }
        final boolean isAlreadyBorrowed = historyRepository.isAlreadyBorrowedByUser(equipmentId,user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("The requested equipment is already borrowed");
        }

        EquipmentTransactionHistory equipmentTransactionHistory = EquipmentTransactionHistory.builder()
                .user(user)
                .equipment(equipment)
                .returned(false)
                .returnApproved(false)
                .build();
        return historyRepository.save(equipmentTransactionHistory).getId();
    }

    @Override
    public Integer returnBorrowedBook(Integer equipmentId, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        if(equipment.isArchived() || !equipment.isSharable()){
            throw  new OperationNotPermittedException("The requested equipment is cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
            throw new OperationNotPermittedException("You can't borrow or return your own equipments");
        }
        EquipmentTransactionHistory equipmentTransactionHistory = historyRepository.findByEquipmentIdAndUserId(equipmentId,user.getId())
                .orElseThrow(()-> new OperationNotPermittedException("You did not borrow this equipment"));
        equipmentTransactionHistory.setReturned(true);

        return historyRepository.save(equipmentTransactionHistory).getId();
    }

    @Override
    public Integer approveReturnBorrowedBook(Integer equipmentId, Authentication connectedUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        if(equipment.isArchived() || !equipment.isSharable()){
            throw  new OperationNotPermittedException("The requested equipment is cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(equipment.getOwner().getEquipments(),user.getId())){
            throw new OperationNotPermittedException("You can't borrow or return your own equipments");
        }
        EquipmentTransactionHistory equipmentTransactionHistory = historyRepository.findByEquipmentIdAndOwnerId(equipmentId,user.getId())
                .orElseThrow(()-> new OperationNotPermittedException("You equipment is not returned yet! so you can't approve it's return"));
        equipmentTransactionHistory.setReturnApproved(true);
        return historyRepository.save(equipmentTransactionHistory).getId();
    }

    @Override
    public void uploadEquipmentImage(MultipartFile file, Authentication connectedUser, Integer equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new EntityNotFoundException("No equipment found with Id :: "+equipmentId));
        User user = ((User) connectedUser.getPrincipal());
        var equipmentImage = fileStorageService.saveFile(file,user.getId());
        equipment.setImage(equipmentImage);
        equipmentRepository.save(equipment);
    }
}
