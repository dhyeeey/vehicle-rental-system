package org.intech.vehiclerental.mappers;

import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.InterfaceVehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleDetailsRecord;
import org.intech.vehiclerental.dto.vehicledto.VehicleImageRecord;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.VehicleImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "fuelType", source = "fuelType")
    @Mapping(target = "transmissionType", source = "transmissionType")
    @Mapping(target = "status", source = "status")
    VehicleDetailsRecord toVehicleDetailsRecordFromVehicle(Vehicle vehicle);

    Vehicle toVehicleFromVehicleRegistrationDTO(VehicleRegistrationDTO vehicleRegistrationDTO);

    VehicleImageRecord toVehicleImageRecordFromVehicleImage(VehicleImage image);

    Set<VehicleImageRecord> toVehicleImageRecordSetFromSetVehicleImage(Set<VehicleImage> images);

    VehicleDetailsRecord toVehicleDetailsRecordFromInterfaceVehicleInfo(InterfaceVehicleInfo projection);

    VehicleImageRecord toVehicleImageRecordFromInterFaceVehicleImageInfo(
            InterfaceVehicleInfo.InterFaceVehicleImageInfo imageProjection
    );

    Set<VehicleImageRecord> toSetVehicleImageRecordFromSetInterFaceVehicleImageInfo(
            Set<InterfaceVehicleInfo.InterFaceVehicleImageInfo> images
    );
}