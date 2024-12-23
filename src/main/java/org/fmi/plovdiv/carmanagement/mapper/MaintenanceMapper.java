package org.fmi.plovdiv.carmanagement.mapper;

import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    Maintenance toEntity(CreateMaintenanceDTO dto);

    Maintenance toEntity(UpdateMaintenanceDTO dto);

    ResponseMaintenanceDTO toDto(Maintenance entity);
}
