package com.ashish.fitness.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
    @Query(
            """
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.equipment.id = :equipmentId
            """
    )
    Page<Feedback> findAllByEquipmentId(Integer equipmentId, Pageable pageable);
}
