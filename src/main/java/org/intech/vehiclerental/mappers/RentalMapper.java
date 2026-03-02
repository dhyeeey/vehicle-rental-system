package org.intech.vehiclerental.mappers;

import org.intech.vehiclerental.dto.rentaldto.CreateRentalResponseDto;
import org.intech.vehiclerental.models.Rental;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    CreateRentalResponseDto toCreateRentalResponseDtoFromRental(Rental rental);
}
