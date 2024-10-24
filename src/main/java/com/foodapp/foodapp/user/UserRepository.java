package com.foodapp.foodapp.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(final String email);

    @Query("SELECT u FROM User u JOIN u.companies c WHERE c.id = :companyId")
    List<User> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT c.id FROM Company c JOIN c.companyUsers u WHERE c.id = :companyId)")
    List<User> findUsersNotBelongingToCompany(@Param("companyId") Long companyId);
}
