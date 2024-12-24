package org.fmi.plovdiv.carmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMaintenanceDTO {

    @NotNull(message = "Car ID is required")
    @Positive(message = "Car ID must be positive")
    private Long carId;

    @NotEmpty(message = "Service type is required")
    private String serviceType;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    @NotNull(message = "Garage ID is required")
    @Positive(message = "Garage ID must be positive")
    private Long garageId;
}
