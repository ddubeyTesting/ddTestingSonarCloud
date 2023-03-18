package com.zemoso.figma.growthcapital.service;

import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ContractService {

    public Contract createNewContract(ContractDto contractDto);

    public List<Contract> getAllContracts();

    public Contract getContractById(Long contractId) throws ResourceNotFoundException;

    public Contract updateContract(ContractDto contractDto, Long id) throws ResourceNotFoundException;

    public void deleteContractById(Long contractId) throws ResourceNotFoundException;

    public List<Contract> getContractsByIds(List<Long> contractIds);
}
