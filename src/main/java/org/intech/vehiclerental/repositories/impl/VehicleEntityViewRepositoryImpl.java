package org.intech.vehiclerental.repositories.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.VehicleEntityViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class VehicleEntityViewRepositoryImpl implements VehicleEntityViewRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    @Autowired
    public VehicleEntityViewRepositoryImpl(EntityManager em,
                                           CriteriaBuilderFactory cbf,
                                           EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }


    @Override
    public Optional<VehicleInfo> findVehicleInfoById(Long id) {

        CriteriaBuilder<Vehicle> cb =
                cbf.create(em, Vehicle.class)
                        .where("id").eq(id);

        VehicleInfo result = evm.applySetting(
                EntityViewSetting.create(VehicleInfo.class),
                cb
        ).getSingleResultOrNull();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<AccountOwner> findVehicleOwnerByVehicleId(Long vehicleId) {

        AccountOwner owner = cbf.create(em, AccountOwner.class)
                .from(Vehicle.class)
                .select("accountOwner")
                .where("id").eq(vehicleId)
                .getSingleResultOrNull();

        return Optional.ofNullable(owner);
    }


    @Override
    public Optional<Vehicle> findVehicleEntityWithOwnerById(Long id) {

        Vehicle vehicle = cbf.create(em, Vehicle.class)
                .fetch("accountOwner")
                .where("id").eq(id)
                .getSingleResultOrNull();

        return Optional.ofNullable(vehicle);
    }


    @Override
    public PagedList<VehicleFleetDto> findVehicleFleetPageByOwner(
            AccountOwner owner,
            VehicleStatus status,
            Boolean isAvailable,
            Pageable pageable
    ) {
        CriteriaBuilder<Vehicle> cb = cbf.create(em, Vehicle.class)
                .where("accountOwner").eq(owner);

        if (status != null) {
            cb.where("status").eq(status);
        }

        if (isAvailable != null) {
            cb.where("isAvailable").eq(isAvailable);
        }

        cb.orderByDesc("createdAt")
                .orderByAsc("id");

        EntityViewSetting<VehicleFleetDto, ?> setting =
                EntityViewSetting.create(VehicleFleetDto.class);

        PaginatedCriteriaBuilder<VehicleFleetDto> pageCb =
                evm.applySetting(setting, cb)
                        .page((int)pageable.getOffset(), pageable.getPageSize());

        PagedList<VehicleFleetDto> pagedList = pageCb.getResultList();

        return pagedList;
    }


    @Override
    public List<VehicleSearchInfo> findVehicleSearchList(
            String location,
            Long minPrice,
            Long maxPrice,
            Integer minSeats
    ) {

        CriteriaBuilder<Vehicle> cb =
                cbf.create(em, Vehicle.class);

        if (location != null) {
            cb.where("location")
                    .like()
                    .value("%" + location + "%");
        }

        if (minPrice != null) {
            cb.where("pricePerDay").ge(minPrice);
        }

        if (maxPrice != null) {
            cb.where("pricePerDay").le(maxPrice);
        }

        if (minSeats != null) {
            cb.where("seatingCapacity").ge(minSeats);
        }

        cb.where("isAvailable").eq(true);

        CriteriaBuilder<VehicleSearchInfo> viewCb =
                evm.applySetting(
                        EntityViewSetting.create(VehicleSearchInfo.class),
                        cb
                );

        return viewCb.getResultList();
    }


    @Override
    public Set<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(AccountOwner owner) {

        CriteriaBuilder<Vehicle> cb =
                cbf.create(em, Vehicle.class)
                        .where("accountOwner").notEq(owner)
                        .where("isAvailable").eq(true);

        CriteriaBuilder<VehicleSearchInfo> viewCb =
                evm.applySetting(
                        EntityViewSetting.create(VehicleSearchInfo.class),
                        cb
                );

        List<VehicleSearchInfo> list = viewCb.getResultList();

        return new HashSet<>(list);
    }


    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {

        if (vehicle.getId() == null) {
            em.persist(vehicle);
            return vehicle;
        } else {
            return em.merge(vehicle);
        }
    }

    @Override
    public void deleteVehicleById(Long id) {

        Vehicle vehicle = em.find(Vehicle.class, id);

        if (vehicle != null) {
            em.remove(vehicle);
        }
    }
}