package com.ashish.fitness.equipment;

import com.ashish.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Equipment extends BaseEntity {

    private String equipmentName;
    private String companyName;
    private String isbn;
    private String description;
    private String image;
    private boolean archived;
    private boolean sharable;



}
