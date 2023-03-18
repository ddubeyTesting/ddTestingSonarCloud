package com.zemoso.figma.growthcapital.repository;

import com.zemoso.figma.growthcapital.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(
            value = "SELECT * FROM payments u WHERE u.status = 'UPCOMING'",
            nativeQuery = true)
    Optional<List<Payment>> findByUserId(Long userId);
}
