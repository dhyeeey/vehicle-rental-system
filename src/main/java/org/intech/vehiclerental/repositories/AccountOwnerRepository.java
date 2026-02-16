package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.entities.AccountOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountOwnerRepository extends JpaRepository<AccountOwner, Long> {

    @Query("select ao from AccountOwner ao where ao.id = ?1")
    Optional<AccountOwner> findById(Long aLong);


}
