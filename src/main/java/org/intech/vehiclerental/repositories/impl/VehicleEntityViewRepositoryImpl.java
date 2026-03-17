package org.intech.vehiclerental.repositories.impl;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.spi.type.DirtyStateTrackable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Company;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.VehicleEntityViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;

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
    public int updateVehicleStatus(Long vehicleId, VehicleStatus status, AccountOwner accountOwner) {

        return cbf.update(em, Vehicle.class)
                .set("status", status)
                .where("id").eq(vehicleId)
                .where("accountOwner").eq(accountOwner)
                .executeUpdate();
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


    public CriteriaBuilder<Vehicle> findSearchVehicleCB(
            boolean isAvailable,VehicleStatus vehicleStatus, VehicleApprovalStatus vehicleApprovalStatus
    ){
        CriteriaBuilder<Vehicle> cb =
                cbf.create(em, Vehicle.class)
                        .where("isAvailable").eq(isAvailable)
                        .where("status").eq(vehicleStatus)
                        .where("approvalStatus").eq(vehicleApprovalStatus);

        return cb;
    }


    @Override
    public Set<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(AccountOwner owner) {

        CriteriaBuilder<Vehicle> cb = findSearchVehicleCB(true, VehicleStatus.ACTIVE, VehicleApprovalStatus.APPROVED);

        if(owner != null){
            cb.where("accountOwner").notEq(owner);
        }

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
    public int deleteVehicleById(Long id, AccountOwner owner) {
        Vehicle vehicle = cbf.create(em, Vehicle.class)
                .where("id").eq(id)
                .where("accountOwner").eq(owner)
                .getSingleResultOrNull();

        if (vehicle == null) {
            return 0;
        }

        em.remove(vehicle);

        return 1;
    }

    @Override
    public Vehicle findVehicleById(Long vehicleId) {
        CriteriaBuilder<Vehicle> cb =  cbf.create(em,Vehicle.class).where("id").eq(vehicleId);
        return cb.getSingleResult();
    }

    private <T> boolean setIfChanged(T current,
                                     T updated,
                                     Consumer<T> setter) {

        if (!Objects.equals(current, updated)) {
            setter.accept(updated);
            return true;
        }

        return false;
    }


    @Override
    public int changeVehicleApprovalStatus(Long vehicleId,
                                   VehicleStatus vehicleStatus,
                                   VehicleApprovalStatus vehicleApprovalStatus,
                                   AccountOwner accountOwner){

        VehicleStatusUpdateView vehicleView =
                evm.find(em, VehicleStatusUpdateView.class, vehicleId);

        if (vehicleView == null) {
            return 0;
        }

        vehicleView.setStatus(vehicleStatus);
        vehicleView.setApprovalStatus(vehicleApprovalStatus);

        if (accountOwner != null) {
            VehicleStatusUpdateView.AccountOwnerIdView ownerRef =
                    evm.getReference(VehicleStatusUpdateView.AccountOwnerIdView.class, accountOwner.getId());
            vehicleView.setApprovedBy(ownerRef);
        } else {
            vehicleView.setApprovedBy(null);
        }

        evm.save(em, vehicleView);

        return 1;

    }

    @Override
    public PagedList<VehicleListViewAdmin> getVehicleListForAdminAndCompanyByStatus(
            VehicleStatus vehicleStatus,
            VehicleApprovalStatus vehicleApprovalStatus,int page,
            int size
    ) {
        CriteriaBuilder<Vehicle> cb = cbf.create(em, Vehicle.class);

        if (vehicleStatus != null) {
            cb.where("status").eq(vehicleStatus);
        }

        if (vehicleApprovalStatus != null) {
            cb.where("approvalStatus").eq(vehicleApprovalStatus);
        }

        cb.orderByDesc("createdAt")
                .orderByAsc("id");

        EntityViewSetting<VehicleListViewAdmin, ?> setting =
                EntityViewSetting.create(VehicleListViewAdmin.class);

        PaginatedCriteriaBuilder<VehicleListViewAdmin> pageCb =
                evm.applySetting(setting, cb)
                        .page(page * size, size); // offset = page * size

        return pageCb.getResultList();
    }

}