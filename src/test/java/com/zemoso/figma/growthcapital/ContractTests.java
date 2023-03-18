package com.zemoso.figma.growthcapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.figma.growthcapital.controller.impl.ContractController;
import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.ContractMapper;
import com.zemoso.figma.growthcapital.service.ContractService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ContractController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContractTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    @MockBean
    private ContractService contractService;

    @Autowired
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void getContractById_success() throws Exception {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();

        given(contractService.getContractById(id)).willReturn(ContractMapper.mapToContract(contractDto));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/contracts/{id}", id));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("contract1")))
                .andExpect(jsonPath("$.type", is("monthly")));
    }

    @Test
    public void getContractById_fail() throws Exception {
        Long id = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();

        given(contractService.getContractById(id)).willThrow(new ResourceNotFoundException("Contract not found for the id:" + id));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/contracts/{id}", id));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getAllContracts_test() throws Exception {
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
        given(contractService.getAllContracts()).willReturn(listOfContracts);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/contracts"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfContracts.size())));

    }

    @Test
    public void createContract_test() throws Exception {
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();
        Mockito.when(contractService.createNewContract(contractDto)).thenReturn(ContractMapper.mapToContract(contractDto));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("contract1")))
                .andExpect(jsonPath("$.type", is("monthly")))
                .andExpect(jsonPath("$.paymentAmount", is(126720.0)));
    }

    @Test
    public void updateContract_test() throws Exception {
        long contractId = 1L;
        ContractDto contractDto = ContractDto.builder()
                .name("contract1")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(12000))
                .build();
        ContractDto updatedContract = ContractDto.builder()
                .name("contract2")
                .type("monthly")
                .termLength(12)
                .rate(12)
                .perPayment(BigDecimal.valueOf(5000))
                .build();
        Mockito.when(contractService.updateContract(contractDto, contractId)).thenReturn(ContractMapper.mapToContract(contractDto));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/contracts/{contractId}", contractId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContract));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteContract_test() throws Exception {
        long contractId = 1L;
        willDoNothing().given(contractService).deleteContractById(contractId);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/contracts/{id}", contractId));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
