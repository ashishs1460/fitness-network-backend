package com.ashish.fitness.equipment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedEquipmentResponse {

    private Integer id;
    private String equipmentName;
    private String isbn;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
