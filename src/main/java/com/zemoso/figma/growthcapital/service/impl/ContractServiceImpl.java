package com.zemoso.figma.growthcapital.service.impl;

import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.ContractMapper;
import com.zemoso.figma.growthcapital.repository.ContractRepository;
import com.zemoso.figma.growthcapital.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Contract createNewContract(ContractDto contractDto) {
        Contract newContract = ContractMapper.mapToContract(contractDto);
        return contractRepository.save(newContract);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract getContractById(Long contractId) throws ResourceNotFoundException {
        return contractRepository.findById(contractId).orElseThrow(() -> new ResourceNotFoundException("Contract not found for the id:" + contractId));
    }

    @Override
    public Contract updateContract(ContractDto contractDto, Long id) throws ResourceNotFoundException {
        Contract existingContract = getContractById(id);
        existingContract.setName(contractDto.getName() != null ? contractDto.getName() : existingContract.getName());
        existingContract.setType(contractDto.getType() != null ? contractDto.getType() : existingContract.getType());
        existingContract.setTermLength(contractDto.getTermLength() != null ? contractDto.getTermLength() : existingContract.getTermLength());
        existingContract.setPerPayment(contractDto.getPerPayment() != null ? contractDto.getPerPayment() : existingContract.getPerPayment());
        existingContract.setRate(contractDto.getRate() != null ? contractDto.getRate() : existingContract.getRate());
        existingContract.setPaymentAmount(calculatePaymentAmount(existingContract.getRate(), existingContract.getTermLength(), existingContract.getPerPayment()));
        return contractRepository.save(existingContract);
    }

    @Override
    public void deleteContractById(Long contractId) throws ResourceNotFoundException {
        Contract existingContract = getContractById(contractId);
        contractRepository.delete(existingContract);
    }

    @Override
    public List<Contract> getContractsByIds(List<Long> contractIds) {
        return contractRepository.findAllById(contractIds);
    }

    private BigDecimal calculatePaymentAmount(Integer rate, Integer termLength, BigDecimal perPayment) {
        BigDecimal totalPayment = perPayment.multiply(BigDecimal.valueOf(termLength));
        BigDecimal rateValue = BigDecimal.valueOf(rate).divide(BigDecimal.valueOf(100));
        BigDecimal amountPerRate = totalPayment.multiply(rateValue);
        return totalPayment.subtract(amountPerRate);
    }
}
