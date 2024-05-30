package com.ashish.fitness.equipment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EquipmentRequest(

        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String equipmentName,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String isbn,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String description,
        boolean sharable

) {
}
