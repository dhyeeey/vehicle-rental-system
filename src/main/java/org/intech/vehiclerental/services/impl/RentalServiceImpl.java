package org.intech.vehiclerental.services.impl;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.rentaldto.RentalViewForRequests;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.repositories.custom.RentalQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.VehicleRepository;
import org.intech.vehiclerental.services.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalQueryRepository rentalRepository;
    private final VehicleRepository vehicleRepository;

    public RentalServiceImpl(RentalQueryRepository rentalRepository, VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public Rental createRental(User renter, CreateRentalRequestDto createRentalRequestDto) {

        Vehicle vehicle = vehicleRepository.findEntityById(createRentalRequestDto.vehicleId())
                .orElseThrow(()->new RuntimeException("Vehicle with provided id not found"));

        if(vehicle.getQuantity() <= 0){
            throw new RuntimeException("Vehicle sold out");
        }

        Duration duration = Duration.between(
                createRentalRequestDto.startDate(),
                createRentalRequestDto.endDate()
        );
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
    @Transactional
    public int changeRentalStatus(Long rentalId, RentalStatus rentalStatus){
        return rentalRepository.changeRentalStatus(rentalId, rentalStatus);
    }

    @Override
    public Boolean isCarOwnerAndLoggedUserSame(Long loggedUserId, Long rentalId){
        return rentalRepository.isCarOwnerAndLoggedUserSame(loggedUserId, rentalId);
    }


    @Override
    public PagedList<RentalListDto> findRentalPageByRenter(User renter,
                                                           RentalStatus status,
                                                           Pageable pageable) {
        return rentalRepository.findRentalPageByRenter(renter, status, pageable);
    }


    @Override
    public PagedList<RentalListDto> findRentalPageByVehicle(Vehicle vehicle,
                                                       RentalStatus status,
                                                       Pageable pageable) {
        return rentalRepository.findRentalPageByVehicle(vehicle, status, pageable);
    }

    @Override
    public List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId){
        return rentalRepository.findRentalRequestsByVehicleId(vehicleId);
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