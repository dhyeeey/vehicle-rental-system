package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = ?1 and u.password = ?2")
    Optional<User> findByEmailAndPassword(String email, String password);

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);

    @Query("select (count(u) > 0) from User u where u.phoneNumber = ?1")
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select (count(u) > 0) from User u where u.licenseNumber = ?1")
    boolean existsByLicenseNumber(String licenseNumber);

}
