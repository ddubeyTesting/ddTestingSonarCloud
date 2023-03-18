package com.zemoso.figma.growthcapital.service;

import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CreditService {

    public UserCreditDetails getUserCreditDetails() throws ResourceNotFoundException;

    public List<Payment> getUserPayments() throws ResourceNotFoundException;

    public UserCreditDetails createUserCreditDetails(UserCreditDetails userCreditDetails);

    public UserCreditDetails getUserCredentialsById(Long userId) throws ResourceNotFoundException;
}
