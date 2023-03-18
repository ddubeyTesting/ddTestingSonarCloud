package com.zemoso.figma.growthcapital.repository;

import com.zemoso.figma.growthcapital.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
