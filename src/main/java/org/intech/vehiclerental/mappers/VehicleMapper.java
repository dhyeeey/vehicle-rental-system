package org.intech.vehiclerental.mappers;

import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.models.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicleFromVehicleRegistrationDTO(VehicleRegistrationDTO vehicleRegistrationDTO);
}