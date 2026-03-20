package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.repositories.custom.AdminQueryRepository;
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
    public List<ListUserAccountAdminView> findUsersListForAdmin(){
        CriteriaBuilder<AccountOwner> cb =
                cbf.create(em, AccountOwner.class).where("role").eq(Role.ROLE_INDIVIDUAL);

        return evm.applySetting(EntityViewSetting.create(ListUserAccountAdminView.class),cb).getResultList();
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

}
