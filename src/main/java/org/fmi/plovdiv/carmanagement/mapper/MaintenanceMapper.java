package org.fmi.plovdiv.carmanagement.mapper;

import org.fmi.plovdiv.carmanagement.dto.ResponseMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "car.make", target = "carName")
    @Mapping(source = "garage.id", target = "garageId")
    @Mapping(source = "garage.name", target = "garageName")
    ResponseMaintenanceDTO toDto(Maintenance entity);
}
