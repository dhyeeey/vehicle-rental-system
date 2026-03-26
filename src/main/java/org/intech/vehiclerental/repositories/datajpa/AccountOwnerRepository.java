package org.intech.vehiclerental.repositories.datajpa;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.intech.vehiclerental.models.AccountOwner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountOwnerRepository extends EntityViewRepository<AccountOwner, Long> {
    @Query("select ao from AccountOwner ao where ao.id = :accountId")
    Optional<AccountOwner> findById(@Param("accountId") Long accountId);
    @Query("select ao from AccountOwner ao where ao.email = :email")
    Optional<AccountOwner> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    AccountOwner save(AccountOwner entity);
    void deleteById(Long id);
}