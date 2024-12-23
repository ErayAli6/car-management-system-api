package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

@Data
public class CreateGarageDTO {

    private String name;

    private String location;

    private String city;

    private int capacity;
}
