package com.ashish.fitness.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentTransactionHistoryRepository extends JpaRepository<EquipmentTransactionHistory,Integer> {
    @Query("""
            SELECT history
            FROM EquipmentTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<EquipmentTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);
    @Query("""
            SELECT history
            FROM EquipmentTransactionHistory history
            WHERE history.equipment.owner.id = :userId
            """)
    Page<EquipmentTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query(
            """
            SELECT 
            (COUNT(*) > 0) AS isBorrowed
            FROM EquipmentTransactionHistory equipmentTransactionHistory
            WHERE equipmentTransactionHistory.user.id = :userId 
            AND equipmentTransactionHistory.equipment.id = :equipmentId
            AND equipmentTransactionHistory.returnApproved = false 
            """
    )
    boolean isAlreadyBorrowedByUser(Integer equipmentId, Integer userId);

    @Query(
            """
            SELECT transaction
            FROM EquipmentTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.equipment.id = :equipmentId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """
    )
    Optional< EquipmentTransactionHistory> findByEquipmentIdAndUserId(Integer equipmentId, Integer userId);

    @Query(
            """
            SELECT transaction
            FROM EquipmentTransactionHistory transaction
            WHERE transaction.equipment.owner.id = :userId
            AND transaction.equipment.id = :equipmentId
            AND transaction.returned = true
            AND transaction.returnApproved = false 
            """
    )
    Optional<EquipmentTransactionHistory> findByEquipmentIdAndOwnerId(Integer equipmentId, Integer id);
}
