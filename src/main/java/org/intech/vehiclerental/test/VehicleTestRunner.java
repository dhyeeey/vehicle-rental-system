package org.intech.vehiclerental.test;

import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.entities.Company;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.entities.Vehicle;
import org.intech.vehiclerental.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleTestRunner implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleTestRunner(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) {

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
}
