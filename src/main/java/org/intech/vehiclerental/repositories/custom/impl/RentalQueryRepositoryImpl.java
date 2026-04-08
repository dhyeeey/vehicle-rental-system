package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.intech.vehiclerental.dto.rentaldto.ExistingReviewView;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.rentaldto.RentalViewForRequests;
import org.intech.vehiclerental.dto.requestbody.SubmitReviewPayload;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.repositories.custom.RentalQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.RentalRepository;
import org.intech.vehiclerental.repositories.datajpa.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalQueryRepositoryImpl implements RentalQueryRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalQueryRepositoryImpl(EntityManager em,
                                     CriteriaBuilderFactory cbf,
                                     EntityViewManager evm,
                                     VehicleRepository vehicleRepository,
                                     RentalRepository rentalRepository) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
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

    @Override
    public ExistingReviewView fetchExistingReviewOfRental(Long rentalId, Long userId){
        CriteriaBuilder<Review> cb = cbf.create(em, Review.class)
                .where(Review_.RENTAL+"."+Rental_.ID)
                .eq(rentalId)
                .where(Review_.RENTAL+"."+Rental_.RENTER+"."+User_.ID).eq(userId);

        return evm.applySetting(
                EntityViewSetting.create(ExistingReviewView.class),cb
        ).getSingleResultOrNull();

    }

    @Override
    public void addRentalReview(SubmitReviewPayload payload, Long userId){

        // Fetch rental with ownership check
        Rental rental = em.createQuery("""
            SELECT r FROM Rental r
            WHERE r.id = :rentalId
            AND r.renter.id = :userId
            """, Rental.class)
                .setParameter("rentalId", payload.rentalId())
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Rental not found or not owned by user")
                );

        // Ensure completed
        if (rental.getStatus() != RentalStatus.COMPLETED) {
            throw new RuntimeException("Rental is not completed yet");
        }

        // Prevent duplicate review
        boolean alreadyReviewed = em.createQuery("""
            SELECT 1 FROM Review r 
            WHERE r.rental.id = :rentalId 
            AND r.reviewer.id = :userId
            """)
                .setParameter("rentalId", payload.rentalId())
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .isPresent();

        if (alreadyReviewed) {
            throw new RuntimeException("Review already exists");
        }

        // Validate rating
        if (payload.rating() == null || payload.rating() < 1 || payload.rating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Create review
        Review review = Review.builder()
                .rating(payload.rating())
                .comment(payload.comment())
                .rental(rental)
                .reviewer(rental.getRenter())
                .vehicle(rental.getVehicle())
                .build();

        // Persist
        em.persist(review);
    }

    public Boolean checkIfVehicleIsOwnedByUser(Long vehicleId, Long userId){
        return !cbf.create(em, Long.class)
                .from(Vehicle.class, "v")
                .select("1L")
                .where("v.id").eq(vehicleId)
                .where("v.accountOwner.id").eq(userId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    public List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId, Long userId) {

        if(!checkIfVehicleIsOwnedByUser(vehicleId, userId)){
            throw new RuntimeException("Unauthorized access to vehicle");
        }

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
                ).page((int)pageable.getOffset(), pageable.getPageSize());

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
                ).page((int)pageable.getOffset(), pageable.getPageSize());

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

    public void deleteReview(Long reviewId, Long userId){
        Review review = em.createQuery("""
            SELECT r FROM Review r
            WHERE r.id = :reviewId
            AND r.reviewer.id = :userId
            """, Review.class)
                .setParameter("reviewId", reviewId)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Review not found or not owned by user")
                );

        em.remove(review);
    }

    public void addVehicleReviewForRental(
            Long rentalId, Long renterId, Float rating,
            String comment
    ){

        // Validate rating
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Fetch rental with conditions (ownership check included)
        Rental rental = em.createQuery("""
            SELECT r FROM Rental r
            JOIN FETCH r.vehicle v
            WHERE r.id = :rentalId
            AND r.renter.id = :renterId
            """, Rental.class)
                .setParameter("rentalId", rentalId)
                .setParameter("renterId", renterId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Rental not found or does not belong to user")
                );

        // Ensure rental is completed
        if (rental.getStatus() != RentalStatus.COMPLETED) {
            throw new RuntimeException("Cannot review a rental that is not completed");
        }

        // Prevent duplicate review (1 review per rental)
        boolean alreadyReviewed = em.createQuery("""
            SELECT 1 FROM Review r WHERE r.rental.id = :rentalId and r.reviewer.id = :renterId
            """)
                .setParameter("rentalId", rentalId)
                .setParameter("renterId",renterId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .isEmpty();

        if (alreadyReviewed) {
            throw new RuntimeException("Review already exists for this rental from user");
        }

        // Create review
        Review review = Review.builder()
                .rating(rating)
                .comment(comment)
                .vehicle(rental.getVehicle())
                .rental(rental)
                .reviewer(rental.getRenter())
                .build();

        // Persist
        em.persist(review);
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
    public int changeRentalStatus(Long rentalId, RentalStatus rentalStatus, Long userId){

        if(!isCarOwnerAndLoggedUserSame(userId,rentalId)){
            throw new AccessDeniedException("You are not allowed to modify this rental");
        }

        Rental rental = em.find(Rental.class, rentalId);
        rental.setStatus(rentalStatus);

        if(rentalStatus == RentalStatus.CONFIRMED){
            Vehicle vehicle = rental.getVehicle();
            vehicle.setIsAvailable(false);
        }

        return 1;
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