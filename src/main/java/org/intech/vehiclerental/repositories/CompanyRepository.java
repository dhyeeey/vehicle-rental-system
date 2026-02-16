package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("select c from Company c where c.email = ?1")
    Optional<Company> findByEmail(String email);

    @Query("select c from Company c where c.id = ?1")
    Optional<Company> findById(Long aLong);

    @Query("select (count(c) > 0) from Company c where c.id = ?1")
    boolean existsById(Long id);

}
