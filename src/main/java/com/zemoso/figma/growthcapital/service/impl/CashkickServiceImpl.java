package com.zemoso.figma.growthcapital.service.impl;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.*;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.CashkickMapper;
import com.zemoso.figma.growthcapital.repository.CashkickRepository;
import com.zemoso.figma.growthcapital.service.CashkickService;
import com.zemoso.figma.growthcapital.service.ContractService;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CashkickServiceImpl implements CashkickService {
    @Autowired
    private CashkickRepository cashkickRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditService creditService;

    @Autowired
    @Lazy
    private ContractService contractService;

    private static final String TOTAL_FINANCED = "totalFinanced";
    private static final String TOTAL_RECEIVED = "totalReceived";

    @Override
    public Cashkick createNewCashkick(CashkickDto cashkickDto) throws ResourceNotFoundException {
        Cashkick newCashkick = CashkickMapper.mapToCashkick(cashkickDto);
        newCashkick.setContracts(contractService.getContractsByIds(cashkickDto.getContractIds()));
        Map<String, BigDecimal> cashkickAmountDetails = getCashkickAmountDetails(newCashkick.getContracts());
        newCashkick.setTotalReceived(cashkickAmountDetails.get(TOTAL_FINANCED));
        newCashkick.setTotalFinanced(cashkickAmountDetails.get(TOTAL_RECEIVED));
        newCashkick.setUser(userService.getUserById(cashkickDto.getUserId()));
        updateUserCreditsAfterCashkick(newCashkick.getUser(), newCashkick.getTotalFinanced());
        return cashkickRepository.save(newCashkick);
    }

    @Override
    public List<Cashkick> getAllCashkicks() {
        return cashkickRepository.findAll();
    }

    @Override
    public Cashkick getCashkickById(Long cashkickId) throws ResourceNotFoundException {
        return cashkickRepository.findById(cashkickId).orElseThrow(() -> new ResourceNotFoundException("Cashkick not found for the id:" + cashkickId));
    }

    @Override
    public Cashkick updateCashkick(CashkickDto cashkickDto, Long cashkickId) throws ResourceNotFoundException {
        Cashkick existingCashkick = getCashkickById(cashkickId);
        existingCashkick.setName(cashkickDto.getName() != null ? cashkickDto.getName() : existingCashkick.getName());
        existingCashkick.setUser(cashkickDto.getUserId() != null ? userService.getUserById(cashkickDto.getUserId()) : existingCashkick.getUser());
        existingCashkick.setContracts(cashkickDto.getContractIds().isEmpty() ? existingCashkick.getContracts() : contractService.getContractsByIds(cashkickDto.getContractIds()));
        existingCashkick.setStatus(cashkickDto.getStatus() != null ? cashkickDto.getStatus() : existingCashkick.getStatus());
        Map<String, BigDecimal> cashkickAmountDetails = getCashkickAmountDetails(existingCashkick.getContracts());
        existingCashkick.setTotalReceived(cashkickAmountDetails.get(TOTAL_FINANCED));
        existingCashkick.setTotalFinanced(cashkickAmountDetails.get(TOTAL_RECEIVED));
        updateUserCreditsAfterCashkick(existingCashkick.getUser(), existingCashkick.getTotalFinanced());
        return cashkickRepository.save(existingCashkick);
    }

    @Override
    public void deleteCashkickById(Long cashkickId) throws ResourceNotFoundException {
        Cashkick existingCashkick = getCashkickById(cashkickId);
        updateUserCreditsAfterCashkickDelete(existingCashkick.getUser(), existingCashkick.getTotalFinanced());
        cashkickRepository.delete(existingCashkick);
    }

    @Override
    public List<Cashkick> getUserCashKicks(Long userId) throws ResourceNotFoundException {
        return cashkickRepository
                .findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User Cashkicks not found for the userId:" + userId));
    }

    private Map<String, BigDecimal> getCashkickAmountDetails(List<Contract> contractList) {
        BigDecimal totalFinanced = BigDecimal.valueOf(0.0);
        BigDecimal totalReceived = BigDecimal.valueOf(0.0);
        for (Contract contract : contractList) {
            totalReceived = totalReceived.add(contract.getPaymentAmount());
            totalFinanced = totalFinanced.add(contract.getPerPayment().multiply(BigDecimal.valueOf(contract.getTermLength())));
        }
        Map<String, BigDecimal> cashkickAmountDetailsMap = new HashMap<>();
        cashkickAmountDetailsMap.put(TOTAL_FINANCED, totalFinanced);
        cashkickAmountDetailsMap.put(TOTAL_RECEIVED, totalReceived);
        return cashkickAmountDetailsMap;
    }

    private void updateUserCreditsAfterCashkick(User user, BigDecimal totalFinanced) throws ResourceNotFoundException {
        UserCreditDetails userCreditDetails = creditService.getUserCredentialsById(user.getId());
        userCreditDetails.setOutstandingAmount(user.getUserCreditDetails().getOutstandingAmount().add(totalFinanced));
        userCreditDetails.setAvailableCredit(user.getUserCreditDetails().getAvailableCredit().subtract(totalFinanced));
        creditService.createUserCreditDetails(userCreditDetails);
    }

    private void updateUserCreditsAfterCashkickDelete(User user, BigDecimal totalFinanced) throws ResourceNotFoundException {
        UserCreditDetails userCreditDetails = creditService.getUserCredentialsById(user.getId());
        userCreditDetails.setOutstandingAmount(user.getUserCreditDetails().getOutstandingAmount().subtract(totalFinanced));
        userCreditDetails.setAvailableCredit(user.getUserCreditDetails().getAvailableCredit().add(totalFinanced));
        creditService.createUserCreditDetails(userCreditDetails);
    }
}
