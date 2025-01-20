package com.foodapp.foodapp.auth.jwtToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query(value = """
      select t from JwtToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<JwtToken> findAllValidTokenByUser(Long id);

    Optional<JwtToken> findByToken(String token);

    @Modifying
    @Query(value = "UPDATE jwt_token SET revoked = :revoked WHERE _user_id = :userId", nativeQuery = true)
    void updateRevokedByUserId(@Param("revoked") boolean revoked, @Param("userId") Long userId);
}