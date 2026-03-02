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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;


/**
 * Runner class for inserting dummy data for testing purposes
 */
@Component
public class VehicleTestRunner implements CommandLineRunner {

    private final AccountOwnerRepository accountOwnerRepository;
    private final VehicleEntityViewRepository vehicleEntityViewRepository;

    @Autowired
    public VehicleTestRunner(
            AccountOwnerRepository accountOwnerRepository,
            VehicleEntityViewRepository vehicleEntityViewRepository
    ) {
        this.accountOwnerRepository = accountOwnerRepository;
        this.vehicleEntityViewRepository = vehicleEntityViewRepository;
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
