package com.foodapp.foodapp.administration.company.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyCustomRepository {
    Optional<Company> findById(Long companyId);

    Optional<Company> findByName(String name);
}
