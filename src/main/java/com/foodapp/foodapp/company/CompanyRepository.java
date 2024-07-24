package com.foodapp.foodapp.company;

import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findById(Integer token);


}
