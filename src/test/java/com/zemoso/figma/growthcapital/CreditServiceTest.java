package com.zemoso.figma.growthcapital;


import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.UserCreditDetailsMapper;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.CreditRepository;
import com.zemoso.figma.growthcapital.repository.PaymentRepository;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import com.zemoso.figma.growthcapital.service.impl.CreditServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CreditService creditService = new CreditServiceImpl();

    @Mock
    private UserService userService;

    @Test
    public void testGetUserCredentials() throws ResourceNotFoundException {
        long userId = 1L;
        UserDto existingUser = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        User currentUser = UserMapper.mapToUser(existingUser);
        currentUser.setId(userId);
        when(userService.getLoggedInUser()).thenReturn(currentUser);
        when(creditRepository.findByUserId(userId)).thenReturn(Optional.of(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails)));
        UserCreditDetails currentUserCredentials = creditService.getUserCreditDetails();
        Assertions.assertEquals("$", currentUserCredentials.getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(8800000), currentUserCredentials.getAvailableCredit());
    }

    @Test
    public void testGetUserPayments() throws ResourceNotFoundException {
        long userId = 1L;
        UserDto user = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        User currentUser = UserMapper.mapToUser(user);
        currentUser.setId(userId);
        when(userService.getLoggedInUser()).thenReturn(currentUser);
        Payment payment = Payment.builder().dueDate(new Date(System.currentTimeMillis()))
                .user(UserMapper.mapToUser(UserDto.builder().email("johndoe@gmail.com").build()))
                .expectedAmount(BigDecimal.valueOf(12000)).
                outstandingAmount(BigDecimal.valueOf(5000)).build();
        payment.setUser(currentUser);
        List<Payment> userPayments = new ArrayList<>();
        userPayments.add(payment);
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(Optional.of(userPayments));
        List<Payment> currentUserPayments = creditService.getUserPayments();
        Assertions.assertEquals(1, currentUserPayments.size());
        Assertions.assertEquals(BigDecimal.valueOf(12000), currentUserPayments.get(0).getExpectedAmount());
    }

    @Test
    public void testCreateCreditDetails() {
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        UserCreditDetails userCredentials = UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails);
        when(creditRepository.save(Mockito.any(UserCreditDetails.class))).thenReturn(userCredentials);
        UserCreditDetails newCreditDetails = creditService.createUserCreditDetails(userCredentials);
        Assertions.assertEquals("$", newCreditDetails.getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(8800000), newCreditDetails.getAvailableCredit());
    }

    @Test
    public void testGetUserCredentialsById() throws ResourceNotFoundException {
        long userId = 1L;
        UserDto existingUser = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        when(creditRepository.findByUserId(userId)).thenReturn(Optional.of(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails)));
        UserCreditDetails currentUserCredentials = creditService.getUserCredentialsById(userId);
        Assertions.assertEquals("$", currentUserCredentials.getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(8800000), currentUserCredentials.getAvailableCredit());
    }
}
