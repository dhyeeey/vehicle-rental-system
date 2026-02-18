package org.intech.vehiclerental.controllers;

import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;
    private final AccountOwner accountOwner;

    @Autowired
    public VehicleController(VehicleService vehicleService, AccountOwner accountOwner){
        this.vehicleService = vehicleService;
        this.accountOwner = accountOwner;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerNewVehicle(){
        return null;
    }
}
