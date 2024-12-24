package org.fmi.plovdiv.carmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "maintenance_requests")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Service type is required")
    private String serviceType;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;
}
