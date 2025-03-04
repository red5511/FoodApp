package com.foodapp.foodapp.order.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class CustomOrderIdGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long generate(Order order) {
        Long companyId = order.getCompany().getId();
        String sequenceName = "company_" + companyId + "_order_seq";

        // Create sequence if it doesn't exist
        String createSequenceSQL = "DO $$ " +
                "BEGIN " +
                "IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = '" + sequenceName + "') " +
                "THEN CREATE SEQUENCE " + sequenceName + "; " +
                "END IF; " +
                "END $$;";
        entityManager.createNativeQuery(createSequenceSQL).executeUpdate();

        // Fetch next value from sequence
        String nextValSQL = "SELECT nextval('" + sequenceName + "')";
        Long nextId = (Long) entityManager.createNativeQuery(nextValSQL).getSingleResult();

        return nextId;
    }
}
