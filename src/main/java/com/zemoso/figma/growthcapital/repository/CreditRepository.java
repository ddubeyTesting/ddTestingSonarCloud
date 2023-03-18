package com.zemoso.figma.growthcapital.repository;

import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<UserCreditDetails, Long> {
    Optional<UserCreditDetails> findByUserId(Long userId);
}
