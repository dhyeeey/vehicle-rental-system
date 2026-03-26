package org.intech.vehiclerental.runners;

import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleWithAccountOwnerView;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.repositories.custom.AccountOwnerQueryRepository;
import org.intech.vehiclerental.repositories.custom.VehicleQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Runner class for inserting dummy data for testing purposes
 */
//@Component
public class VehicleTestRunner implements CommandLineRunner {

    private final AccountOwnerQueryRepository accountOwnerQueryRepository;
    private final VehicleQueryRepository vehicleQueryRepository;

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleTestRunner(
            AccountOwnerQueryRepository accountOwnerQueryRepository,
            VehicleQueryRepository vehicleQueryRepository,
            VehicleRepository vehicleRepository
    ) {
        this.accountOwnerQueryRepository = accountOwnerQueryRepository;
        this.vehicleQueryRepository = vehicleQueryRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void testing(){
        Optional<VehicleInfo> vehicleInfo = vehicleRepository.findInfoViewById(2L);

        vehicleInfo.ifPresent((v)->{
            System.out.println("VehicleInfo Projection : "+v.getId()+" "+v.getMake()+" "+v.getModel());
        });

        Optional<Vehicle> vehicle = vehicleRepository.findEntityById(3L);

        vehicle.ifPresent((v)->{
            System.out.println("Vehicle entity : "+v.getId()+" "+v.getMake()+" "+v.getModel());
        });

        Optional<VehicleWithAccountOwnerView> vehicleWithAccountOwnerView = vehicleRepository.findWithOwnerViewById(4L);

        vehicleWithAccountOwnerView.ifPresent((v)->{
            System.out.println("VehicleWithAccountOwnerView Projection : "+v.getId()+" "+v.getMake()+" "+v.getModel());
        });
    }

    @Override
    public void run(String... args) {
        testing();
    }

}
