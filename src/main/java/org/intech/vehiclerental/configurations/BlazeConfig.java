package org.intech.vehiclerental.configurations;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.intech.vehiclerental.dto.rentaldto.*;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleImageView;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlazeConfig {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    EntityViewConfiguration entityViewConfiguration() {
        EntityViewConfiguration cfg = EntityViews.createDefaultConfiguration();

         cfg.addEntityView(VehicleFleetDto.class);
         cfg.addEntityView(VehicleImageView.class);
         cfg.addEntityView(VehicleInfo.class);
         cfg.addEntityView(VehicleSearchInfo.class);

         cfg.addEntityView(RentalInfo.class);
         cfg.addEntityView(RentalListDto.class);
         cfg.addEntityView(RentalUserDetail.class);
         cfg.addEntityView(RentalVehicleDetail.class);
         cfg.addEntityView(RentalVehicleSummary.class);
         cfg.addEntityView(RentalViewForRequests.class);
         cfg.addEntityView(UserViewForRentalRequest.class);

        return cfg;
    }

    @Bean
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

    @Bean
    public EntityViewManager createEntityViewManager(CriteriaBuilderFactory cbf, EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(cbf);
    }
}
