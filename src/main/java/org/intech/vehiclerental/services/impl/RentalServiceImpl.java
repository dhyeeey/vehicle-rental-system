package org.intech.vehiclerental.services.impl;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.rentaldto.*;
import org.intech.vehiclerental.dto.requestbody.SubmitReviewPayload;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.repositories.custom.RentalQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.RentalRepository;
import org.intech.vehiclerental.repositories.datajpa.VehicleRepository;
import org.intech.vehiclerental.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalQueryRepository rentalQueryRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalServiceImpl(
            RentalQueryRepository rentalQueryRepository,
            VehicleRepository vehicleRepository,
            RentalRepository rentalRepository
    ) {
        this.rentalQueryRepository = rentalQueryRepository;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public Rental createRental(User renter, CreateRentalRequestDto createRentalRequestDto) {

        Vehicle vehicle = vehicleRepository.findEntityById(createRentalRequestDto.vehicleId())
                .orElseThrow(()->new RuntimeException("Vehicle with provided id not found"));

        if(!vehicle.getIsAvailable()){
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
                .scheduledStartDateTime(createRentalRequestDto.startDate())
                .scheduledEndDateTime(createRentalRequestDto.endDate())
                .status(RentalStatus.PENDING)
                .build();

        return rentalQueryRepository.saveRental(rental);
    }

    @Override
    public Optional<RentalInfo> findRentalInfoById(Long id) {
        return rentalQueryRepository.findRentalInfoById(id);
    }

    @Override
    public RentalDetailViewForRentalRequest findRentalDetailViewForRentalRequest(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(()->new RuntimeException("Rental with id " + rentalId + " not found"));
    }

    @Override
    @Transactional
    public int changeRentalStatus(Long rentalId, RentalStatus rentalStatus, Long userId){
        return rentalQueryRepository.changeRentalStatus(rentalId, rentalStatus, userId);
    }

    @Override
    public ReviewData fetchExistingReviewOfRental(Long rentalId, Long userId){
        ExistingReviewView existingReviewView = rentalQueryRepository.fetchExistingReviewOfRental(rentalId, userId);
        return new ReviewData(
                Objects.nonNull(existingReviewView),
                existingReviewView
        );
    }

    @Override
    @Transactional
    public void addRentalReview(SubmitReviewPayload payload, Long userId){
        rentalQueryRepository.addRentalReview(payload, userId);
    }

    @Override
    public PagedList<RentalListDto> findRentalPageByRenter(User renter,
                                                           RentalStatus status,
                                                           Pageable pageable) {
        return rentalQueryRepository.findRentalPageByRenter(renter, status, pageable);
    }


    @Override
    public PagedList<RentalListDto> findRentalPageByVehicle(Vehicle vehicle,
                                                       RentalStatus status,
                                                       Pageable pageable) {
        return rentalQueryRepository.findRentalPageByVehicle(vehicle, status, pageable);
    }

    @Override
    public List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId, Long userId){
        return rentalQueryRepository.findRentalRequestsByVehicleId(vehicleId, userId);
    }

    @Override
    public Rental saveRental(Rental rental) {
        return rentalQueryRepository.saveRental(rental);
    }

    @Override
    public void deleteRentalById(Long id) {
        rentalQueryRepository.deleteRentalById(id);
    }
}