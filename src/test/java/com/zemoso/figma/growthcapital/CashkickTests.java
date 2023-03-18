package com.zemoso.figma.growthcapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.figma.growthcapital.controller.impl.CashkickController;
import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.CashkickMapper;
import com.zemoso.figma.growthcapital.service.CashkickService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CashkickController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CashkickTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    @MockBean
    private CashkickService cashkickService;
    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    @MockBean
    private ContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void getCashkickById_success() throws Exception {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();

        given(cashkickService.getCashkickById(id)).willReturn(CashkickMapper.mapToCashkick(cashkickDto));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/cashkicks/{id}", id));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("cashkick1")));
    }

    @Test
    public void getCashkickById_fail() throws Exception {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L, 2L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();

        given(cashkickService.getCashkickById(id)).willThrow(new ResourceNotFoundException("Cashkick not found for the id:" + id));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/cashkicks/{id}", id));
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getAllCashkickTest() throws Exception {
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
        given(cashkickService.getAllCashkicks()).willReturn(listOfCashkicks);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/cashkicks"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfCashkicks.size())));

    }

    @Test
    public void createCashkickTest() throws Exception {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        Mockito.when(cashkickService.createNewCashkick(cashkickDto)).thenReturn(CashkickMapper.mapToCashkick(cashkickDto));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/cashkicks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cashkickDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("cashkick1")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    public void updateCashkickTest() throws Exception {
        Long id = 1L;
        Long[] contractIds = new Long[]{1L};
        CashkickDto cashkickDto = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .build();
        CashkickDto updatedCashkick = CashkickDto.builder()
                .name("cashkick1")
                .userId(id)
                .contractIds(Arrays.asList(contractIds))
                .status("APPROVED")
                .build();
        Mockito.when(cashkickService.updateCashkick(cashkickDto, id)).thenReturn(CashkickMapper.mapToCashkick(cashkickDto));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/cashkicks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCashkick));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCashkickTest() throws Exception {
        // given - precondition or setup
        long id = 1L;
        willDoNothing().given(cashkickService).deleteCashkickById(id);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/cashkicks/{id}", id));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}
