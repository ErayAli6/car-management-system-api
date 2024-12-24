package org.fmi.plovdiv.carmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Make is required")
    private String make;

    @NotEmpty(message = "Model is required")
    private String model;

    @Positive(message = "Production year must be positive")
    private int productionYear;

    @NotEmpty(message = "License plate is required")
    @Size(max = 10, message = "License plate cannot exceed 10 characters")
    private String licensePlate;

    @ManyToMany
    @JoinTable(
            name = "car_garage",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private List<Garage> garages = new ArrayList<>();
}
