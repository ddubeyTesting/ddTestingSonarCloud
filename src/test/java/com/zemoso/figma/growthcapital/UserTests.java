package com.zemoso.figma.growthcapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.exceptions.GlobalExceptionHandler;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.CashkickMapper;
import com.zemoso.figma.growthcapital.mapper.ContractMapper;
import com.zemoso.figma.growthcapital.mapper.UserCreditDetailsMapper;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.UserRepository;
import com.zemoso.figma.growthcapital.service.CashkickService;
import com.zemoso.figma.growthcapital.service.ContractService;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
@RunWith(SpringRunner.class)
public class UserTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CreditService creditService;

    @MockBean
    private ContractService contractService;

    @MockBean
    private CashkickService cashkickService;

    @MockBean
    private UserService userService;

    @Test
    public void getUserById_success() throws Exception {
        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id));
        response.andExpect(status().isOk());
    }

    @Test(expected = MockitoException.class)
    public void getUserById_fail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup()
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        given(userRepository.findById(id)).willThrow(new ResourceNotFoundException("User not found for the id:" + id));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getAllEmployees_test() throws Exception {
        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(UserMapper.mapToUser(UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build()));
        listOfUsers.add(UserMapper.mapToUser(UserDto.builder()
                .name("Kim")
                .email("kim@gmail.com")
                .password("kim121")
                .build()));
        listOfUsers.add(UserMapper.mapToUser(UserDto.builder()
                .name("jim")
                .email("jim@gmail.com")
                .password("jim121")
                .build()));
        when(userRepository.findAll()).thenReturn(listOfUsers);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfUsers.size())));

    }

    @Test
    public void updateUser_test() throws Exception {
        // given - precondition or setup
        Long id = 1L;
        UserDto existingUser = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        UserDto updatedUser = UserDto.builder()
                .name("Ram")
                .email("ram@gmail.com").password("12345")
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(UserMapper.mapToUser(existingUser)));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserMapper.mapToUser(updatedUser));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteUser_test() throws Exception {
        // given - precondition or setup
        long userId = 1L;
        willDoNothing().given(userRepository).delete(Mockito.any(User.class));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId));
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void createUserCredentials_test() throws Exception {
        long userId = 1L;
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        Mockito.when(creditService.createUserCreditDetails(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails))).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/users/{userId}/creditDetails", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreditDetails));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUserCredentials_test() throws Exception {
        long userId = 1L;
        UserCreditDetailsDto userCreditDetails = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).build();
        UserCreditDetailsDto UpdateduserCreditDetails = UserCreditDetailsDto.builder().currency("INR").availableCredit(BigDecimal.valueOf(8900000)).build();
        Mockito.when(creditService.createUserCreditDetails(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetails))).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(UpdateduserCreditDetails));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users/{userId}/creditDetails", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreditDetails));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getUserCashkicks_test() throws Exception {
        List<Cashkick> cashkicks = new ArrayList<>();
        long userId = 1L;
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance1").userId(userId).build()));
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance2").userId(userId).build()));
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance3").userId(userId).build()));
        when(cashkickService.getUserCashKicks(userId)).thenReturn(cashkicks);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/cashkicks", userId));
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getUserContracts_test() throws Exception {
        List<Contract> listOfContracts = new ArrayList<>();
        listOfContracts.add(ContractMapper.mapToContract(ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build()));
        listOfContracts.add(ContractMapper.mapToContract(ContractDto.builder()
                .name("contract2")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build()));
        listOfContracts.add(ContractMapper.mapToContract(ContractDto.builder()
                .name("contract3")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build()));
        long id = 1L;
        given(userService.getUserContracts(id)).willReturn(listOfContracts);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/contracts", id));
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
