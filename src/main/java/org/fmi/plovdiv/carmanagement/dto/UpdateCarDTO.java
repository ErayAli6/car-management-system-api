package org.fmi.plovdiv.carmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCarDTO {

    @NotEmpty(message = "Make is required")
    private String make;

    @NotEmpty(message = "Model is required")
    private String model;

    @Positive(message = "Production year must be positive")
    private int productionYear;

    @NotEmpty(message = "License plate is required")
    @Size(max = 10, message = "License plate cannot exceed 10 characters")
    private String licensePlate;

    @NotNull(message = "Garage IDs are required")
    private List<Long> garageIds;
}
