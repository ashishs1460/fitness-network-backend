package com.ashish.fitness.equipment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentResponse {

    private Integer id;
    private String equipmentName;
    private String isbn;
    private String description;
    private String owner;
    private byte[] image;
    private double rate;
    private boolean archived;
    private boolean sharable;
}
