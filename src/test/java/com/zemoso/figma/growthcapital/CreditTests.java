package com.zemoso.figma.growthcapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.figma.growthcapital.controller.impl.CreditController;
import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.mapper.UserCreditDetailsMapper;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CreditController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreditTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    @MockBean
    private CreditService creditService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void getUserCredentials_test() throws Exception {
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        Mockito.when(creditService.getUserCreditDetails()).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/creditDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest.with(httpBasic("johndoe@gmail.com", "john121")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.availableCredit", is(8800000)))
                .andExpect(jsonPath("$.currency", is("$")));
    }

    @Test
    public void getUserPayments_test() throws Exception {
        Payment payment = Payment.builder().dueDate(new Date(System.currentTimeMillis()))
                .user(UserMapper.mapToUser(UserDto.builder().email("johndoe@gmail.com").build()))
                .expectedAmount(BigDecimal.valueOf(12000)).
                outstandingAmount(BigDecimal.valueOf(5000)).build();
        List<Payment> userPayments = new ArrayList<>();
        userPayments.add(payment);
        Mockito.when(creditService.getUserPayments()).thenReturn(userPayments);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest.with(httpBasic("johndoe@gmail.com", "john121")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userPayments.size())));
    }
}
