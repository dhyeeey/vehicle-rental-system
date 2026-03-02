package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.repositories.RentalEntityViewRepository;
import org.intech.vehiclerental.services.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalEntityViewRepository rentalRepository;

    public RentalServiceImpl(RentalEntityViewRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental createRental(User renter, Vehicle vehicle, CreateRentalRequestDto createRentalRequestDto) {

        Duration duration = Duration.between(createRentalRequestDto.startDate(), createRentalRequestDto.endDate());
        Double durationInDays = (double)duration.toDays();

        Double baseAmount = vehicle.getPricePerDay()*durationInDays;
        Double depositAmount = 0d;
        Double discountAmount = 0d;
        Double taxAmount = baseAmount*0.10;
        Double totalAmount = baseAmount + depositAmount + taxAmount - discountAmount;

        if (!vehicle.getIsAvailable()) {
            throw new RuntimeException("Vehicle is not available");
        }

        Rental rental = Rental.builder()
                .renter(renter)
                .vehicle(vehicle)
                .baseAmount(baseAmount)
                .depositAmount(depositAmount)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .actualStartDateTime(createRentalRequestDto.startDate())
                .actualEndDateTime(createRentalRequestDto.endDate())
                .status(RentalStatus.PENDING)
                .build();

        vehicle.setQuantity((byte) (vehicle.getQuantity()-1));
        vehicle.setIsAvailable(vehicle.getQuantity() > 0);

        return rentalRepository.saveRental(rental);
    }

    @Override
    public Optional<RentalInfo> findRentalInfoById(Long id) {
        return rentalRepository.findRentalInfoById(id);
    }


    @Override
    public Page<RentalListDto> findRentalPageByRenter(User renter,
                                                      RentalStatus status,
                                                      Pageable pageable) {
        return rentalRepository.findRentalPageByRenter(renter, status, pageable);
    }


    @Override
    public Page<RentalListDto> findRentalPageByVehicle(Vehicle vehicle,
                                                       RentalStatus status,
                                                       Pageable pageable) {
        return rentalRepository.findRentalPageByVehicle(vehicle, status, pageable);
    }


    @Override
    public Rental saveRental(Rental rental) {
        return rentalRepository.saveRental(rental);
    }


    @Override
    public void deleteRentalById(Long id) {
        rentalRepository.deleteRentalById(id);
    }
}