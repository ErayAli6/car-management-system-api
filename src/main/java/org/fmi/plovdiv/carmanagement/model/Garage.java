package org.fmi.plovdiv.carmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotEmpty(message = "City is required")
    private String city;

    @Positive(message = "Capacity must be positive")
    private int capacity;

    @ManyToMany(mappedBy = "garages")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "garage")
    private List<Maintenance> maintenances = new ArrayList<>();
}
