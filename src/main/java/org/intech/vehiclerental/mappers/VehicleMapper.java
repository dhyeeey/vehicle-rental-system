package org.intech.vehiclerental.mappers;

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
    VehicleDetailsRecord toVehicleDetails(Vehicle vehicle);

    VehicleImageRecord toVehicleImageRecord(VehicleImage image);

    Set<VehicleImageRecord> toVehicleImageRecordSet(Set<VehicleImage> images);

    VehicleDetailsRecord toRecord(InterfaceVehicleInfo projection);

    VehicleImageRecord toImageRecord(
            InterfaceVehicleInfo.InterFaceVehicleImageInfo imageProjection
    );

    Set<VehicleImageRecord> toImageRecordSet(
            Set<InterfaceVehicleInfo.InterFaceVehicleImageInfo> images
    );
}