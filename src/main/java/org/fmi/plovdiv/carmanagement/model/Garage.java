package org.fmi.plovdiv.carmanagement.model;

import jakarta.persistence.*;
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

    private String name;

    private String location;

    private String city;

    private int capacity;

    @ManyToMany(mappedBy = "garages")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "garage")
    private List<Maintenance> maintenances = new ArrayList<>();
}
