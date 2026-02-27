package org.intech.vehiclerental.runners;

import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Company;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.models.enums.VehicleType;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.repositories.VehicleEntityViewRepository;
import org.intech.vehiclerental.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;


/**
 * Runner class for inserting dummy data for testing purposes
 */
@Component
public class VehicleTestRunner implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final AccountOwnerRepository accountOwnerRepository;
    private final VehicleEntityViewRepository vehicleEntityViewRepository;

    @Autowired
    public VehicleTestRunner(
            VehicleRepository vehicleRepository,
            AccountOwnerRepository accountOwnerRepository,
            VehicleEntityViewRepository vehicleEntityViewRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.accountOwnerRepository = accountOwnerRepository;
        this.vehicleEntityViewRepository = vehicleEntityViewRepository;
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
                .mileage(9).pricePerDay(500L)
                .accountOwner(accountOwner)
                .location("Gandhinagar").build();

        vehicleRepository.save(vehicle);
    }

    public void fetchallvehicles(){
        AccountOwner accountOwner = accountOwnerRepository.findById(3L).orElseThrow(()->new RuntimeException("Account owner not found"));

//        Set<Vehicle> vehicles = vehicleRepository.findByAccountOwnerNot(accountOwner);
//
//        for (Vehicle vh : vehicles){
//            System.out.println(vh.getId()+", "+vh.getMake()+", "+vh.getModel());
//        }
    }


    @Override
    public void run(String... args) {

//        insertvehicle();
//        fetchallvehicles();

    }

}
