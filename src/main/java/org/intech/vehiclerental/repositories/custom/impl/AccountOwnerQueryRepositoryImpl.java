package org.intech.vehiclerental.repositories.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.UpdateCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.intech.vehiclerental.dto.authdto.AuthUserProjection;
import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.AccountOwner_;
import org.intech.vehiclerental.models.Company;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.repositories.custom.AccountOwnerQueryRepository;
import org.intech.vehiclerental.repositories.utility.Predicates;
import org.intech.vehiclerental.repositories.utility.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountOwnerQueryRepositoryImpl implements AccountOwnerQueryRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    @Autowired
    public AccountOwnerQueryRepositoryImpl(
            EntityManager em,
            CriteriaBuilderFactory cbf,
            EntityViewManager evm
    ) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    public Optional<AccountOwner> findById(Long id) {
        return Optional.ofNullable(
                QueryBuilder.build(
                    cbf,em ,AccountOwner.class,
                    Predicates.eq(AccountOwner_.ID, id)
                ).getSingleResultOrNull()
        );
    }

    @Override
    public Optional<AuthUserProjection> findAuthDetailsByEmail(String email) {

        Tuple tuple = cbf.create(em, Tuple.class)
                .from(AccountOwner.class)
                .select(AccountOwner_.ID)
                .select(AccountOwner_.EMAIL)
                .select(AccountOwner_.PASSWORD)
                .select(AccountOwner_.ROLE)
                .select(AccountOwner_.ACCOUNT_STATUS)
                .where(AccountOwner_.EMAIL).eq(email)
                .getSingleResultOrNull();

        if (tuple == null) return Optional.empty();

        return Optional.of(new AuthUserProjection(
                tuple.get(0, Long.class),
                tuple.get(1, String.class),
                tuple.get(2, String.class),
                tuple.get(3, Role.class),
                tuple.get(4, AccountStatus.class)
        ));
    }

    private boolean updateCommonFields(UpdateCriteriaBuilder<?> update,
                                       AccountOwner accountOwner,
                                       EditAccountProfileDto dto) {

        boolean hasChanges = false;

        if (dto.phoneNumber() != null && !dto.phoneNumber().equals(accountOwner.getPhoneNumber())) {
            update.set("phoneNumber", dto.phoneNumber());
            hasChanges = true;
        }

        if (dto.address() != null && !dto.address().equals(accountOwner.getAddress())) {
            update.set("address", dto.address());
            hasChanges = true;
        }

        if (dto.city() != null && !dto.city().equals(accountOwner.getCity())) {
            update.set("city", dto.city());
            hasChanges = true;
        }

        if (dto.state() != null && !dto.state().equals(accountOwner.getState())) {
            update.set("state", dto.state());
            hasChanges = true;
        }

        if (dto.country() != null && !dto.country().equals(accountOwner.getCountry())) {
            update.set("country", dto.country());
            hasChanges = true;
        }

        if (dto.zipCode() != null && !dto.zipCode().equals(accountOwner.getZipCode())) {
            update.set("zipCode", dto.zipCode());
            hasChanges = true;
        }

        return hasChanges;
    }

    @Override
    public int editProfileDetails(Long accountOwnerId, EditAccountProfileDto dto) {

        UpdateCriteriaBuilder<?> update;

        AccountOwner accountOwner = findById(accountOwnerId).orElseThrow(()->new RuntimeException("Account with provided id not found"));

        if (accountOwner instanceof User user) {

            update = cbf.update(em, User.class)
                    .where("id").eq(user.getId());

            boolean hasChanges = false;

            if (dto.firstName() != null && !dto.firstName().isEmpty()
                    && !dto.firstName().equals(user.getFirstName())) {

                update.set("firstName", dto.firstName());
                hasChanges = true;
            }

            if (dto.lastName() != null && !dto.lastName().isEmpty()
                    && !dto.lastName().equals(user.getLastName())) {

                update.set("lastName", dto.lastName());
                hasChanges = true;
            }

            hasChanges |= updateCommonFields(update, accountOwner, dto);

            return hasChanges ? update.executeUpdate() : 0;
        }

        if (accountOwner instanceof Company company) {

            update = cbf.update(em, Company.class)
                    .where("id").eq(company.getId());

            boolean hasChanges = false;

            if (dto.name() != null && !dto.name().isEmpty()
                    && !dto.name().equals(company.getName())) {

                update.set("name", dto.name());
                hasChanges = true;
            }

            hasChanges |= updateCommonFields(update, accountOwner, dto);

            return hasChanges ? update.executeUpdate() : 0;
        }

        return 0;
    }


    @Override
    public int editProfileImage(AccountOwner accountOwner, String imageUrl) {
        UpdateCriteriaBuilder<AccountOwner> update = cbf.update(em, AccountOwner.class)
                .where("id").eq(accountOwner.getId());

        if (imageUrl == null) {
            update.setExpression("profileImageUrl", "NULL");
        } else {
            update.set("profileImageUrl", imageUrl);
        }

        return update.executeUpdate();
    }

    @Override
    public String getCurrentProfileImageUrl(
            Long accountOwnerId
    ){
        Tuple tuple = cbf.create(em, Tuple.class)
                .from(AccountOwner.class,"ao")
                .select("ao.profileImageUrl")
                .where("ao.id").eq(accountOwnerId)
                .getSingleResult();

        return tuple.get(0, String.class);
    }

    @Override
    public int editProfileImage(Long accountOwnerId, String imageUrl) {

        UpdateCriteriaBuilder<AccountOwner> update = cbf.update(em, AccountOwner.class)
                .where("id").eq(accountOwnerId);

        if (imageUrl == null) {
            update.setExpression("profileImageUrl", "NULL");
        } else {
            update.set("profileImageUrl", imageUrl);
        }

        return update.executeUpdate();
    }


}