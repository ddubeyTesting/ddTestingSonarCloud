package com.zemoso.figma.growthcapital;

import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.ContractMapper;
import com.zemoso.figma.growthcapital.repository.ContractRepository;
import com.zemoso.figma.growthcapital.service.ContractService;
import com.zemoso.figma.growthcapital.service.impl.ContractServiceImpl;
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
public class ContractServiceTests {
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService = new ContractServiceImpl();

    @Test
    public void testGetAllContracts() {
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
        given(contractRepository.findAll()).willReturn(listOfContracts);
        List<Contract> contracts = contractService.getAllContracts();
        Assertions.assertEquals(3, contracts.size());
    }

    @Test
    public void testGetContractById() throws ResourceNotFoundException {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();
        given(contractRepository.findById(id)).willReturn(Optional.of(ContractMapper.mapToContract(contractDto)));
        Contract contract = contractService.getContractById(id);
        Assertions.assertEquals("contract1", contract.getName());
        Assertions.assertEquals("monthly", contract.getType());

    }

    @Test
    public void testCreateContract() {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();
        given(contractRepository.save(Mockito.any(Contract.class))).willReturn((ContractMapper.mapToContract(contractDto)));
        Contract contract = contractService.createNewContract(contractDto);
        Assertions.assertEquals("contract1", contract.getName());
        Assertions.assertEquals("monthly", contract.getType());
        Assertions.assertEquals(contract.getPaymentAmount(), BigDecimal.valueOf(126720.00).setScale(2));
    }

    @Test
    public void testUpdateContract() throws ResourceNotFoundException {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();
        ContractDto newContractDto = ContractDto.builder()
                .name("contract2")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(5000))
                .build();
        given(contractRepository.save(Mockito.any(Contract.class))).willReturn((ContractMapper.mapToContract(newContractDto)));
        given(contractRepository.findById(id)).willReturn(Optional.of((ContractMapper.mapToContract(contractDto))));
        Contract contract = contractService.updateContract(contractDto, id);
        Assertions.assertEquals("contract2", contract.getName());
        Assertions.assertEquals("monthly", contract.getType());
        Assertions.assertEquals(contract.getPaymentAmount(), BigDecimal.valueOf(52800).setScale(2));
    }

    @Test
    public void testDeleteContract() throws ResourceNotFoundException {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();

        when(contractRepository.findById(id)).thenReturn(Optional.of(ContractMapper.mapToContract(contractDto)));
        willDoNothing().given(contractRepository).delete(Mockito.any(Contract.class));
        contractService.deleteContractById(id);
        verify(contractRepository, times(1)).delete(ContractMapper.mapToContract(contractDto));
    }

    @Test
    public void testGetContractByIds() {
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
        given(contractRepository.findAllById(Arrays.asList(new Long[]{1L, 2L, 3L}))).willReturn(listOfContracts);
        List<Contract> contracts = contractService.getContractsByIds(Arrays.asList(new Long[]{1L, 2L, 3L}));
        Assertions.assertEquals(contracts.size(), listOfContracts.size());
    }
}
