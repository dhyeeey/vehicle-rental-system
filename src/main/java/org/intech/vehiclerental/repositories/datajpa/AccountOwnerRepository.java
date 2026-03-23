package org.intech.vehiclerental.repositories.datajpa;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.intech.vehiclerental.models.AccountOwner;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountOwnerRepository extends EntityViewRepository<AccountOwner, Long> {
    Optional<AccountOwner> findById(Long accountId);
    Optional<AccountOwner> findByEmail(String email);
    boolean existsByEmail(String email);
    AccountOwner save(AccountOwner entity);
    void deleteById(Long id);
}
