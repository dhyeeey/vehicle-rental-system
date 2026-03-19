package org.intech.vehiclerental.mappers;

import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetUpdateView;
import org.intech.vehiclerental.dto.vehicledto.VehicleUpdateFormData;
import org.intech.vehiclerental.models.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicleFromVehicleRegistrationDTO(VehicleRegistrationDTO vehicleRegistrationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVehicleFromDto(
            VehicleUpdateFormData dto,
            @MappingTarget VehicleFleetUpdateView view
    );
}