package org.intech.vehiclerental.repositories.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.persistence.EntityManager;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class AccountOwnerRepositoryImpl implements AccountOwnerRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;

    @Autowired
    public AccountOwnerRepositoryImpl(
            EntityManager em,
            CriteriaBuilderFactory cbf
    ) {
        this.em = em;
        this.cbf = cbf;
    }

    @Override
    public Optional<AccountOwner> findById(Long id) {

        AccountOwner owner = cbf.create(em, AccountOwner.class)
                .where("id").eq(id)
                .getSingleResultOrNull();

        return Optional.ofNullable(owner);
    }

    @Override
    public Optional<AccountOwner> findByEmail(String email) {

        AccountOwner owner = cbf.create(em, AccountOwner.class)
                .where("email").eq(email)
                .getSingleResultOrNull();

        return Optional.ofNullable(owner);
    }

    @Override
    public boolean existsByEmail(String email) {

        Long count = cbf.create(em, Long.class)
                .from(AccountOwner.class)
                .select("COUNT(id)")
                .where("email").eq(email)
                .getSingleResult();

        return count > 0;
    }

    @Override
    @Transactional
    public AccountOwner save(AccountOwner accountOwner) {

        if (accountOwner.getId() == null) {
            em.persist(accountOwner);
            return accountOwner;
        } else {
            return em.merge(accountOwner);
        }
    }
}