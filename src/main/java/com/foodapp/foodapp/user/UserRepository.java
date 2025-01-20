package com.foodapp.foodapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(final String email);

    @Query("SELECT u FROM User u JOIN u.companies c WHERE c.id = :companyId")
    List<User> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT c.id FROM Company c JOIN c.users u WHERE c.id = :companyId)")
    List<User> findUsersNotBelongingToCompany(@Param("companyId") Long companyId);

    @Query("UPDATE User u SET u.locked = true WHERE u.id = :userId")
    @Modifying
    void blockUserById(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.locked = false WHERE u.id = :userId")
    void unblockUserById(@Param("userId") Long userId);
}
