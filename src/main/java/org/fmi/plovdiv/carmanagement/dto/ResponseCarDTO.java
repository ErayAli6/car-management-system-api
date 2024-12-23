package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseCarDTO {

    private Long id;

    private String make;

    private String model;

    private int productionYear;

    private String licensePlate;

    private List<ResponseGarageDTO> garages;
}
