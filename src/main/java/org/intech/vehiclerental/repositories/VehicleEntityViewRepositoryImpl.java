package org.intech.vehiclerental.repositories;


import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleEntityViewRepositoryImpl {

    private final CriteriaBuilderFactory cbf;
    private final EntityManager entityManager;
    private final EntityViewManager evm;

    @Autowired
    public VehicleEntityViewRepositoryImpl(
            CriteriaBuilderFactory cbf,
            EntityManager entityManager,
            EntityViewManager evm
    ){
        this.cbf = cbf;
        this.entityManager = entityManager;
        this.evm = evm;
    }

}
