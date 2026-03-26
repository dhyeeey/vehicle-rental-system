package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.repositories.custom.AdminQueryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminQueryRepositoryImpl implements AdminQueryRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public AdminQueryRepositoryImpl(
            EntityManager em,
            CriteriaBuilderFactory cbf,
            EntityViewManager evm
    ) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Override
    public PagedList<ListUserAccountAdminView> findUsersListForAdmin(Pageable pageable){
        CriteriaBuilder<AccountOwner> cb =
                cbf.create(em, AccountOwner.class)
                        .where("role").eq(Role.ROLE_INDIVIDUAL)
                        .orderByDesc("id");

        PaginatedCriteriaBuilder<ListUserAccountAdminView> pageCb =
                evm.applySetting(
                        EntityViewSetting.create(ListUserAccountAdminView.class),
                        cb
                ).page(pageable.getOffset(), pageable.getPageSize());

        return pageCb.getResultList();
    }

    public List<RentalInfo> findRentalsOfUserForAdmin(Long userId){
        CriteriaBuilder<Rental> cb = cbf.create(em, Rental.class)
                .where("renter.id").eq(userId);

        return evm.applySetting(EntityViewSetting.create(RentalInfo.class),cb).getResultList();
    }

    public UserDetailAdminDto findUserDetailForAdmin(Long userId){
        CriteriaBuilder<AccountOwner> cb = cbf.create(em, AccountOwner.class).where("id").eq(userId);

        return evm.applySetting(EntityViewSetting.create(UserDetailAdminDto.class), cb).getSingleResult();
    }

    @Override
    public List<VehicleInfo> findUserVehiclesForAdmin(Long userId) {
        CriteriaBuilder<Vehicle> cb = cbf.create(em, Vehicle.class)
                .where("accountOwner.id").eq(userId);

        return evm.applySetting(EntityViewSetting.create(VehicleInfo.class), cb).getResultList();
    }

}
