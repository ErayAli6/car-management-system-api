package org.fmi.plovdiv.carmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateGarageDTO {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotEmpty(message = "City is required")
    private String city;

    @Positive(message = "Capacity must be positive")
    private int capacity;
}
