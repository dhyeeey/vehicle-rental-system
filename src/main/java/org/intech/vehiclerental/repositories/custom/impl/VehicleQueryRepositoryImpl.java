package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.mappers.VehicleMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.custom.VehicleQueryRepository;
import org.intech.vehiclerental.repositories.utility.FilterApplier;
import org.intech.vehiclerental.repositories.utility.VehicleFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Repository
public class VehicleQueryRepositoryImpl implements VehicleQueryRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleQueryRepositoryImpl(EntityManager em,
                                      CriteriaBuilderFactory cbf,
                                      EntityViewManager evm, VehicleMapper vehicleMapper) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
        this.vehicleMapper = vehicleMapper;
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
    public List<VehicleImage> fetchVehicleImagesForEditForm(Long userId, Long vehicleId){
        CriteriaBuilder<VehicleImage> cb = cbf.create(em, VehicleImage.class)
                .where(VehicleImage_.VEHICLE+"."+Vehicle_.ID).eq(vehicleId)
                .where(VehicleImage_.VEHICLE+"."+ Vehicle_.ACCOUNT_OWNER+"."+ AccountOwner_.ID).eq(userId);

        return cb.getResultList();
    }

    @Override
    public int updateVehicleStatus(Long vehicleId, VehicleStatus status, Long accountOwnerId) {

        return cbf.update(em, Vehicle.class)
                .set(Vehicle_.STATUS, status)
                .where(Vehicle_.ID).eq(vehicleId)
                .where(Vehicle_.ACCOUNT_OWNER+"."+AccountOwner_.ID).eq(accountOwnerId)
                .executeUpdate();
    }


    public CriteriaBuilder<Vehicle> findSearchVehicleCB(
            boolean isAvailable,
            VehicleStatus vehicleStatus,
            VehicleApprovalStatus vehicleApprovalStatus
    ){
        CriteriaBuilder<Vehicle> cb =
                cbf.create(em, Vehicle.class)
                        .where("isAvailable").eq(isAvailable)
                        .where("status").eq(vehicleStatus)
                        .where("approvalStatus").eq(vehicleApprovalStatus);

        return cb;
    }

    @Override
    public List<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(Long accountOwnerId, VehicleFilter vehicleFilters) {

        CriteriaBuilder<Vehicle> cb = findSearchVehicleCB(true,
                VehicleStatus.ACTIVE, VehicleApprovalStatus.APPROVED);

        FilterApplier.applyFilters(cb, vehicleFilters);

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
    public int deleteVehicleById(Long vehicleId, Long accountOwnerId) {
        Vehicle vehicle = cbf.create(em, Vehicle.class)
                .where("id").eq(vehicleId)
                .where("accountOwner.id").eq(accountOwnerId)
                .getSingleResultOrNull();

        if (vehicle == null) {
            return 0;
        }

        em.remove(vehicle);

        return 1;
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
            Vehicle vehicle = em.find(Vehicle.class, vehicleId);
            vehicle.getFeatures().clear();
            vehicle.getFeatures().addAll(dto.features());
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