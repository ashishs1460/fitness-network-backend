package com.ashish.fitness.equipment;

import com.ashish.fitness.common.BaseEntity;
import com.ashish.fitness.feedback.Feedback;
import com.ashish.fitness.history.EquipmentTransactionHistory;
import com.ashish.fitness.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Equipment extends BaseEntity {

    private String equipmentName;
    private String isbn;
    private String description;
    private String image;
    private boolean archived;
    private boolean sharable;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "equipment")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "equipment")
    private List<EquipmentTransactionHistory> histories;



}
