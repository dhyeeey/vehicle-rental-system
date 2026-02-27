package org.intech.vehiclerental.mappers;

import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.VehicleImage;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicleFromVehicleRegistrationDTO(VehicleRegistrationDTO vehicleRegistrationDTO);
}