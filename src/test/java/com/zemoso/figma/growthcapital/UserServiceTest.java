package com.zemoso.figma.growthcapital;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
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
import com.zemoso.figma.growthcapital.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    CashkickService cashkickService;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @Mock
    private CreditService creditService;

    @Mock
    private ContractService contractService;

    @Test
    public void testGetAllUsers() {
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
        List<User> users = userService.getAllUsers();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    public void testGetUserById() throws ResourceNotFoundException {
        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));
        User user = userService.getUserById(id);
        Assertions.assertEquals("John Doe", user.getName());
        Assertions.assertEquals("johndoe@gmail.com", user.getEmail());
    }

    @Test
    public void testCreateUser() throws ItemExistsException {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserMapper.mapToUser(userDto));
        User user = userService.createUser(userDto);
        Assertions.assertEquals("John Doe", user.getName());
        Assertions.assertEquals("johndoe@gmail.com", user.getEmail());

    }

    @Test
    public void testUpdateUser() throws ResourceNotFoundException {
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
        User newUser = userService.updateUser(existingUser, id);
        Assertions.assertEquals("Ram", newUser.getName());
        Assertions.assertEquals("ram@gmail.com", newUser.getEmail());
    }

    @Test
    public void testDeleteUser() throws ResourceNotFoundException {
        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));
        willDoNothing().given(userRepository).delete(Mockito.any(User.class));
        userService.deleteUserById(id);
        verify(userRepository, times(1)).delete(UserMapper.mapToUser(userDto));
    }

    @Test
    public void testGetUserCashkicks() throws ResourceNotFoundException {
        List<Cashkick> cashkicks = new ArrayList<>();
        long userId = 1L;
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance1").userId(userId).build()));
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance2").userId(userId).build()));
        cashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder().name("advance3").userId(userId).build()));
        when(cashkickService.getUserCashKicks(userId)).thenReturn(cashkicks);
        Assertions.assertEquals(3, userService.getUserCashkicks(userId).size());
    }

    @Test(expected = MockitoException.class)
    public void testItemAlreadyExistsException() throws ItemExistsException {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        given(userRepository.existsByEmail("johndoe@gmail.com")).willThrow(new ItemExistsException("User is already register with email " + userDto.getEmail()));
        userService.createUser(userDto);
    }

    @Test
    public void testGetUserContracts() throws ResourceNotFoundException {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
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
        Cashkick existingCashkick = CashkickMapper.mapToCashkick(cashkickDto);
        existingCashkick.setContracts(listOfContracts);
        List<Cashkick> userCashkicks = new ArrayList<>();
        userCashkicks.add(existingCashkick);
        lenient().when(cashkickService.getUserCashKicks(id)).thenReturn(userCashkicks);
        List<Contract> userContracts = userService.getUserContracts(id);
        Assertions.assertEquals(listOfContracts.size(), userContracts.size());
    }

    @Test
    public void testCreateUserCredits() throws ResourceNotFoundException {
        UserCreditDetailsDto userCreditDetailsDto = UserCreditDetailsDto.builder()
                .availableCredit(BigDecimal.valueOf(8800000))
                .outstandingAmount(BigDecimal.valueOf(0))
                .currency("$").build();
        UserCreditDetails newUserCreditDetails = UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto);

        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        User newUser = UserMapper.mapToUser(userDto);
        newUser.setId(id);
        newUserCreditDetails.setUser(newUser);

        lenient().when(creditService.createUserCreditDetails(Mockito.any(UserCreditDetails.class))).thenReturn(newUserCreditDetails);
        lenient().when(userRepository.findById(id)).thenReturn(Optional.of(newUser));
        UserCreditDetails finalUserCreditDetails = userService.createUserCreditDetails(userCreditDetailsDto, id);
        Assertions.assertEquals("$", finalUserCreditDetails.getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(8800000), finalUserCreditDetails.getAvailableCredit());
    }

    @Test
    public void testUpdateUserCredits() throws ResourceNotFoundException {
        UserCreditDetailsDto userCreditDetailsDto = UserCreditDetailsDto.builder()
                .availableCredit(BigDecimal.valueOf(8800000))
                .outstandingAmount(BigDecimal.valueOf(0))
                .currency("$").build();
        UserCreditDetails newUserCreditDetails = UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto);

        Long id = 1L;
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        User newUser = UserMapper.mapToUser(userDto);
        newUser.setId(id);
        newUserCreditDetails.setUser(newUser);

        UserCreditDetailsDto updatedUserCreditDetailsDto = UserCreditDetailsDto.builder()
                .availableCredit(BigDecimal.valueOf(8900000))
                .outstandingAmount(BigDecimal.valueOf(0))
                .currency("INR").build();
        UserCreditDetails updatedUserCreditDetails = UserCreditDetailsMapper.mapToUserCreditDetails(updatedUserCreditDetailsDto);
        lenient().when(creditService.getUserCredentialsById(id)).thenReturn(newUserCreditDetails);
        lenient().when(userRepository.findById(id)).thenReturn(Optional.of(newUser));
        lenient().when(creditService.createUserCreditDetails(Mockito.any(UserCreditDetails.class))).thenReturn(updatedUserCreditDetails);

        UserCreditDetails finalUserCreditDetails = userService.updateUserCreditDetails(userCreditDetailsDto, id);

        Assertions.assertEquals("INR", finalUserCreditDetails.getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(8900000), finalUserCreditDetails.getAvailableCredit());
    }

    @Test
    public void testGetLoggedInUser() {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Assertions.assertEquals("johndoe@gmail.com", userService.getLoggedInUser().getEmail());
    }
}
