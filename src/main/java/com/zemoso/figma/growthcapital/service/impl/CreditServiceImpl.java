package com.zemoso.figma.growthcapital.service.impl;

import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.repository.CreditRepository;
import com.zemoso.figma.growthcapital.repository.PaymentRepository;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    @Override
    public UserCreditDetails getUserCreditDetails() throws ResourceNotFoundException {
        Long userId = userService.getLoggedInUser().getId();
        return creditRepository
                .findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User credit details not found for the userId:" + userId));
    }

    @Override
    public List<Payment> getUserPayments() throws ResourceNotFoundException {
        Long userId = userService.getLoggedInUser().getId();
        return paymentRepository
                .findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User Payments not found for the userId:" + userId));
    }

    @Override
    public UserCreditDetails createUserCreditDetails(UserCreditDetails userCreditDetails) {
        return creditRepository.save(userCreditDetails);
    }

    @Override
    public UserCreditDetails getUserCredentialsById(Long userId) throws ResourceNotFoundException {
        return creditRepository
                .findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User credit details not found for the userId:" + userId));
    }
}
