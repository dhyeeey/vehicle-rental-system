package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.rentaldto.RentalViewForRequests;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.Rental_;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.repositories.custom.RentalQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalQueryRepositoryImpl implements RentalQueryRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    @Autowired
    public RentalQueryRepositoryImpl(EntityManager em,
                                     CriteriaBuilderFactory cbf,
                                     EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Override
    public Optional<RentalInfo> findRentalInfoById(Long id) {

        CriteriaBuilder<Rental> cb =
                cbf.create(em, Rental.class)
                        .where("id").eq(id);

        RentalInfo result = evm.applySetting(
                EntityViewSetting.create(RentalInfo.class),
                cb
        ).getSingleResultOrNull();

        return Optional.ofNullable(result);
    }

    public List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId) {

        CriteriaBuilder<Rental> cb =
                cbf.create(em, Rental.class)
                        .where("vehicle.id").eq(vehicleId)
                        .orderByDesc("createdAt")
                        .orderByDesc("id");

        return evm.applySetting(EntityViewSetting.create(RentalViewForRequests.class),cb).getResultList();
    }


    @Override
    public PagedList<RentalListDto> findRentalPageByRenter(
            User renter,
            RentalStatus status,
            Pageable pageable
    ) {

        CriteriaBuilder<Rental> cb =
                cbf.create(em, Rental.class)
                        .where("renter").eq(renter);

        if (status != null) {
            cb.where("status").eq(status);
        }

        cb.orderByDesc("createdAt").orderByDesc("id");

        PaginatedCriteriaBuilder<RentalListDto> pageCb =
                evm.applySetting(
                        EntityViewSetting.create(RentalListDto.class),
                        cb
                ).page(pageable.getOffset(), pageable.getPageSize());

        return pageCb.getResultList();
    }


    @Override
    public PagedList<RentalListDto> findRentalPageByVehicle(
            Vehicle vehicle,
            RentalStatus status,
            Pageable pageable
    ) {

        CriteriaBuilder<Rental> cb =
                cbf.create(em, Rental.class)
                        .where("vehicle").eq(vehicle);

        if (status != null) {
            cb.where("status").eq(status);
        }

        cb.orderByDesc("createdAt").orderByDesc("id");

        PaginatedCriteriaBuilder<RentalListDto> pageCb =
                evm.applySetting(
                        EntityViewSetting.create(RentalListDto.class),
                        cb
                ).page(pageable.getOffset(), pageable.getPageSize());

        return pageCb.getResultList();
    }

    @Override
    public Boolean isCarOwnerAndLoggedUserSame(Long loggedUserId, Long rentalId){
        return !cbf.create(em, Long.class)
                .from(Rental.class, "r")
                .innerJoin("r.vehicle", "v")
                .select("1L")
                .where("r.id").eq(rentalId)
                .where("v.accountOwner.id").eq(loggedUserId)
                .setMaxResults(1)
                .getResultList().isEmpty();
    }

    public Boolean existsOverlappingRental(Long vehicleId, Instant start, Instant end){
        return !cbf.create(em, Long.class)
                .from(Rental.class,"r").select("1L")
                .where("r.vehicle.id").eq(vehicleId)
                .where("r.scheduledStartDateTime").lt(end)
                .where("r.scheduledEndDateTime").gt(start)
                .where("r.status").notIn(RentalStatus.CANCELLED, RentalStatus.COMPLETED)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    @Override
    public int changeRentalStatus(Long rentalId, RentalStatus rentalStatus){
        return cbf.update(em, Rental.class)
                .set(Rental_.STATUS,rentalStatus)
                .where(Rental_.ID).eq(rentalId)
                .executeUpdate();
    }

    @Override
    public Rental saveRental(Rental rental) {
        if (rental.getId() == null) {
            em.persist(rental);
            return rental;
        } else {
            return em.merge(rental);
        }
    }


    @Override
    public void deleteRentalById(Long id) {
        Rental rental = em.find(Rental.class, id);
        if (rental != null) {
            em.remove(rental);
        }
    }
}