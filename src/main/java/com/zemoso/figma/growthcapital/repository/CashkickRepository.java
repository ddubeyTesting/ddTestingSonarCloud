package com.zemoso.figma.growthcapital.repository;

import com.zemoso.figma.growthcapital.entity.Cashkick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashkickRepository extends JpaRepository<Cashkick, Long> {
    Optional<List<Cashkick>> findByUserId(Long userId);
}
