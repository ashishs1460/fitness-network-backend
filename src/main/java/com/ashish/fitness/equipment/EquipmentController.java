package com.ashish.fitness.equipment;

import com.ashish.fitness.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("equipments")
@Tag(name = "equipments")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<Integer> saveEquipment(
            @Valid @RequestBody EquipmentRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.save(request,connectedUser));
    }

    @GetMapping("{equipment-id}")
    public ResponseEntity<EquipmentResponse> findEquipmentById(
            @PathVariable("equipment-id") Integer equipmentId
    ){
        return ResponseEntity.ok(equipmentService.findById(equipmentId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<EquipmentResponse>>findAllEquipments(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser

            ){
        return ResponseEntity.ok(equipmentService.findAllEquipments(page,size,connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<EquipmentResponse>> findAllEquipmentsByOwner(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.findAllEquipmentsByOwner(page,size,connectedUser));
    }
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedEquipmentResponse>> findAllBorrowedEquipments(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.findAllBorrowedEquipments(page,size,connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedEquipmentResponse>> findAllReturnedEquipments(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.findAllReturnedEquipments(page,size,connectedUser));
    }

    @PatchMapping("/shareable/{equipment-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("equipment-id") Integer equipmentId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.updateShareableStatus(equipmentId,connectedUser));
    }

    @PatchMapping("/archived/{equipment-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("equipment-id") Integer equipmentId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.updateArchivedStatus(equipmentId,connectedUser));
    }

    @PostMapping("/borrowed/{equipment-id}")
    public ResponseEntity<Integer> borrowEquipment(
            @PathVariable("equipment-id") Integer equipmentId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.borrowEquipment(equipmentId,connectedUser));
    }

    @PatchMapping("/borrowed/returned/{equipment-id}")
    public ResponseEntity<Integer> returnBorrowedEquipment(
            @PathVariable("equipment-id") Integer equipmentId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.returnBorrowedBook(equipmentId,connectedUser));
    }

    @PatchMapping("/borrowed/returned/approve/{equipment-id}")
    public ResponseEntity<Integer> approveReturnBorrowedEquipment(
            @PathVariable("equipment-id") Integer equipmentId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(equipmentService.approveReturnBorrowedBook(equipmentId,connectedUser));
    }

    @PostMapping(value = "/image/{equipment-id}",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadEquipmentImage(
            @PathVariable("equipment-id") Integer equipmentId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ){
        equipmentService.uploadEquipmentImage(file,connectedUser,equipmentId);
        return  ResponseEntity.accepted().build();
    }





}
