package com.zemoso.figma.growthcapital.mapper;

import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;

import java.math.BigDecimal;

public class ContractMapper {
    public static Contract mapToContract(ContractDto contractDto) {
        Contract contract = new Contract();
        contract.setName(contractDto.getName());
        contract.setType(contractDto.getType());
        contract.setTermLength(contractDto.getTermLength());
        contract.setRate(contractDto.getRate());
        contract.setPerPayment(contractDto.getPerPayment());
        BigDecimal totalPayment = contractDto.getPerPayment().multiply(BigDecimal.valueOf(contractDto.getTermLength()));
        BigDecimal rateValue = BigDecimal.valueOf(contractDto.getRate()).divide(BigDecimal.valueOf(100));
        BigDecimal amountPerRate = totalPayment.multiply(rateValue);
        contract.setPaymentAmount(totalPayment.subtract(amountPerRate));
        return contract;
    }

    private ContractMapper() {

    }
}
