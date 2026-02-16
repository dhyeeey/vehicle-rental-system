package org.intech.vehiclerental.services;

import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.entities.Company;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.entities.Vehicle;
import org.intech.vehiclerental.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public AccountOwner getVehicleOwnerByVehicleId(Long vehicleId){
        Vehicle vehicle = vehicleRepository.findByIdWithOwner(vehicleId).get();
        AccountOwner owner = vehicle.getAccountOwner();

        if (owner instanceof User user) {
            System.out.println(user.getFirstName());
        } else if (owner instanceof Company company) {
            System.out.println(company.getName());
        }

        return owner;
    }


}
