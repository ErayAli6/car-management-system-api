package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;
import org.fmi.plovdiv.carmanagement.model.Garage;

import java.util.List;

@Data
public class UpdateCarDTO {

    private String make;

    private String model;

    private int productionYear;

    private String licensePlate;

    private List<Garage> garages;
}
