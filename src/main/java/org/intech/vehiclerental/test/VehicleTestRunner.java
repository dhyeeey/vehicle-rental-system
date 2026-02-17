package org.intech.vehiclerental.test;

import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.entities.Company;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.entities.Vehicle;
import org.intech.vehiclerental.entities.enums.FuelType;
import org.intech.vehiclerental.entities.enums.TransmissionType;
import org.intech.vehiclerental.entities.enums.VehicleStatus;
import org.intech.vehiclerental.entities.enums.VehicleType;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Component
public class VehicleTestRunner implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public VehicleTestRunner(
            VehicleRepository vehicleRepository,
            AccountOwnerRepository accountOwnerRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    public void getvehicles(){
        System.out.println("====== ALL VEHICLES ======");

        Sort sort=Sort.by(Sort.Direction.ASC,"make");

        Pageable pageReq= PageRequest.of(0, 2, sort);

        List<Vehicle> vehicles = vehicleRepository.findAllWithOwner(pageReq);

        for (Vehicle v : vehicles) {

            AccountOwner owner = v.getAccountOwner();

            System.out.println("Vehicle ID: " + v.getId());
            System.out.println("Make/Model: " + v.getMake() + " " + v.getModel());

            if (owner instanceof User user) {
                System.out.println("Owned by USER: " + user.getFirstName() + " " + user.getLastName());
            }
            else if (owner instanceof Company company) {
                System.out.println("Owned by COMPANY: " + company.getName());
            }

            System.out.println("----------------------------");
        }
    }

    public void insertvehicle(){
        AccountOwner accountOwner = accountOwnerRepository.findById(2L)
                .orElseThrow(()->new RuntimeException("AccountOwner not found"));

        Vehicle vehicle = Vehicle.builder()
                .createdAt(Instant.now())
                .isAvailable(true)
                .year(2022)
                .make("VW")
                .color("Black")
                .description("Fastest VW that you can buy")
                .registrationNumber("GJ18EB0994")
                .vin("3455546")
                .model("Tiguan")
                .type(VehicleType.SEDAN)
                .fuelType(FuelType.PETROL)
                .transmissionType(TransmissionType.AUTOMATIC)
                .status(VehicleStatus.ACTIVE)
                .seatingCapacity(5)
                .mileage(9).pricePerDay(500L).pricePerHour(0L)
                .accountOwner(accountOwner)
                .location("Gandhinagar").build();

        vehicleRepository.save(vehicle);
    }

    @Override
    public void run(String... args) {

//        insertvehicle();
    }
}
