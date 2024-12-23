package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCarDTO {

    private String make;

    private String model;

    private int productionYear;

    private String licensePlate;

    private List<Long> garageIds;
}
