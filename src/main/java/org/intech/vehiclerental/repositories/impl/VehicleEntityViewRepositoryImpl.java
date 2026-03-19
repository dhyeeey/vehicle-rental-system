package org.intech.vehiclerental.repositories.impl;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.spi.type.DirtyStateTrackable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.mappers.VehicleMapper;
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
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleEntityViewRepositoryImpl(EntityManager em,
                                           CriteriaBuilderFactory cbf,
                                           EntityViewManager evm, VehicleMapper vehicleMapper) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
        this.vehicleMapper = vehicleMapper;
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
            Long accountOwnerId,
            VehicleStatus status,
            Boolean isAvailable,
            Pageable pageable
    ) {
        CriteriaBuilder<Vehicle> cb = cbf.create(em, Vehicle.class)
                .where("accountOwner.id").eq(accountOwnerId);

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
    public int updateVehicleStatus(Long vehicleId, VehicleStatus status, Long accountOwnerId) {

        return cbf.update(em, Vehicle.class)
                .set("status", status)
                .where("id").eq(vehicleId)
                .where("accountOwner.id").eq(accountOwnerId)
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
    public List<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(Long accountOwnerId) {

        CriteriaBuilder<Vehicle> cb = findSearchVehicleCB(true,
                VehicleStatus.ACTIVE, VehicleApprovalStatus.APPROVED);

        if(accountOwnerId != null){
            cb.where("accountOwner.id").notEq(accountOwnerId);
        }

        CriteriaBuilder<VehicleSearchInfo> viewCb =
                evm.applySetting(
                        EntityViewSetting.create(VehicleSearchInfo.class),
                        cb
                );

        return viewCb.getResultList();
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
    public int deleteVehicleById(Long id, Long accountOwnerId) {
        Vehicle vehicle = cbf.create(em, Vehicle.class)
                .where("id").eq(id)
                .where("accountOwner.id").eq(accountOwnerId)
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
    public int updateVehiclePartial(Long vehicleId, VehicleUpdateFormData dto) {

        VehicleFleetUpdateView view =
                evm.find(em, VehicleFleetUpdateView.class, vehicleId);

        if (view == null) {
            return 0;
        }

        vehicleMapper.updateVehicleFromDto(dto, view);

        evm.save(em, view);

        em.flush();
        em.clear();

        if (dto.features() != null) {

            em.createNativeQuery("DELETE FROM vehicle_features WHERE vehicle_id = :id")
                    .setParameter("id", vehicleId)
                    .executeUpdate();

            for (String feature : dto.features()) {
                em.createNativeQuery("""
                    INSERT INTO vehicle_features(vehicle_id, feature)
                    VALUES (:vId, :f)
                """)
                        .setParameter("vId", vehicleId)
                        .setParameter("f", feature)
                        .executeUpdate();
            }

        }

        return 1;
    }


    @Override
    public int changeVehicleApprovalStatus(Long vehicleId,
                                   VehicleStatus vehicleStatus,
                                   VehicleApprovalStatus vehicleApprovalStatus,
                                   Long accountOwnerId){

        VehicleStatusUpdateView vehicleView =
                evm.find(em, VehicleStatusUpdateView.class, vehicleId);

        if (vehicleView == null) {
            return 0;
        }

        vehicleView.setStatus(vehicleStatus);
        vehicleView.setApprovalStatus(vehicleApprovalStatus);

        if (accountOwnerId != null) {
            VehicleStatusUpdateView.AccountOwnerIdView ownerRef =
                    evm.getReference(VehicleStatusUpdateView.AccountOwnerIdView.class, accountOwnerId);
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