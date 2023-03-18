package com.zemoso.figma.growthcapital;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.CashkickMapper;
import com.zemoso.figma.growthcapital.mapper.ContractMapper;
import com.zemoso.figma.growthcapital.mapper.UserCreditDetailsMapper;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.CashkickRepository;
import com.zemoso.figma.growthcapital.service.CashkickService;
import com.zemoso.figma.growthcapital.service.ContractService;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import com.zemoso.figma.growthcapital.service.impl.CashkickServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CashkickServiceTests {

    @Mock
    private CashkickRepository cashkickRepository;

    @InjectMocks
    private CashkickService cashkickService = new CashkickServiceImpl();

    @Mock
    private ContractService contractService;

    @Mock
    private CreditService creditService;

    @Mock
    private UserService userService;

    @Test
    public void testGetCashKickById() throws ResourceNotFoundException {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();

        given(cashkickRepository.findById(id)).willReturn(Optional.of(CashkickMapper.mapToCashkick(cashkickDto)));
        Cashkick cashkick = cashkickService.getCashkickById(id);
        Assertions.assertEquals("cashkick1", cashkick.getName());
    }

    @Test
    public void testGetAllCashkicks() {
        List<Cashkick> listOfCashkicks = new ArrayList<>();
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        listOfCashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build()));
        listOfCashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder()
                .name("cashkick2")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build()));
        listOfCashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder()
                .name("cashkick3")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build()));
        given(cashkickRepository.findAll()).willReturn(listOfCashkicks);
        List<Cashkick> cashkicks = cashkickService.getAllCashkicks();
        Assertions.assertEquals(cashkicks.size(), listOfCashkicks.size());
    }

    @Test
    public void testGetUserCashkicks() throws ResourceNotFoundException {
        List<Cashkick> listOfCashkicks = new ArrayList<>();
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        listOfCashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build()));
        listOfCashkicks.add(CashkickMapper.mapToCashkick(CashkickDto.builder()
                .name("cashkick2")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build()));
        given(cashkickRepository.findByUserId(id)).willReturn(Optional.of(listOfCashkicks));
        List<Cashkick> userCashkicks = cashkickService.getUserCashKicks(id);
        Assertions.assertEquals(userCashkicks.size(), listOfCashkicks.size());
    }

    @Test
    public void testCreateCashkick() throws ResourceNotFoundException {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        given(cashkickRepository.save(Mockito.any(Cashkick.class))).willReturn(CashkickMapper.mapToCashkick(cashkickDto));
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
        lenient().when(contractService.getContractsByIds(Arrays.asList(contractIds))).thenReturn(listOfContracts);
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        User newUser = UserMapper.mapToUser(userDto);
        newUser.setId(id);
        UserCreditDetailsDto userCreditDetailsDto = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).outstandingAmount(BigDecimal.valueOf(0.0)).build();
        newUser.setUserCreditDetails(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        lenient().when(userService.getUserById(id)).thenReturn(newUser);
        lenient().when(creditService.getUserCredentialsById(id)).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        lenient().when(creditService.createUserCreditDetails(Mockito.any(UserCreditDetails.class))).thenReturn(new UserCreditDetails());
        Cashkick newCashkick = cashkickService.createNewCashkick(cashkickDto);
        Assertions.assertEquals("cashkick1", newCashkick.getName());
    }

    @Test
    public void testUpdateCashkick() throws ResourceNotFoundException {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        CashkickDto cashkickDto2 = CashkickDto.builder()
                .name("cashkick2")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        given(cashkickRepository.save(Mockito.any(Cashkick.class))).willReturn(CashkickMapper.mapToCashkick(cashkickDto2));
        given(cashkickRepository.findById(id)).willReturn(Optional.of(CashkickMapper.mapToCashkick(cashkickDto)));
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
        lenient().when(contractService.getContractsByIds(Arrays.asList(contractIds))).thenReturn(listOfContracts);
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        User newUser = UserMapper.mapToUser(userDto);
        newUser.setId(id);
        UserCreditDetailsDto userCreditDetailsDto = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).outstandingAmount(BigDecimal.valueOf(0.0)).build();
        newUser.setUserCreditDetails(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        lenient().when(userService.getUserById(id)).thenReturn(newUser);
        lenient().when(creditService.getUserCredentialsById(id)).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        lenient().when(creditService.createUserCreditDetails(Mockito.any(UserCreditDetails.class))).thenReturn(new UserCreditDetails());
        Cashkick newCashkick = cashkickService.updateCashkick(cashkickDto, id);
        Assertions.assertEquals("cashkick2", newCashkick.getName());
    }

    @Test
    public void testDeleteCashkick() throws ResourceNotFoundException {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        User newUser = UserMapper.mapToUser(userDto);
        newUser.setId(id);
        Cashkick existingCashkick = CashkickMapper.mapToCashkick(cashkickDto);
        existingCashkick.setUser(newUser);
        when(cashkickRepository.findById(id)).thenReturn(Optional.of(existingCashkick));
        willDoNothing().given(cashkickRepository).delete(Mockito.any(Cashkick.class));
        UserCreditDetailsDto userCreditDetailsDto = UserCreditDetailsDto.builder().currency("$").availableCredit(BigDecimal.valueOf(8800000)).outstandingAmount(BigDecimal.valueOf(0.0)).build();
        newUser.setUserCreditDetails(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        lenient().when(userService.getUserById(id)).thenReturn(newUser);
        lenient().when(creditService.getUserCredentialsById(id)).thenReturn(UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto));
        cashkickService.deleteCashkickById(id);
        verify(cashkickRepository, times(1)).delete(existingCashkick);
    }
}
