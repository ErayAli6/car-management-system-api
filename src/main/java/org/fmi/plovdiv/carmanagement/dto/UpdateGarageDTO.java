package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

@Data
public class UpdateGarageDTO {

    private String name;

    private String location;

    private int capacity;

    private String city;
}
