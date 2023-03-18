package com.zemoso.figma.growthcapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.figma.growthcapital.controller.impl.AuthController;
import com.zemoso.figma.growthcapital.dto.AuthDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.UserRepository;
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

import java.sql.SQLException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    @MockBean
    private UserService userService;

    @Test
    public void registerUser_success() throws Exception {
        UserDto user = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        User newUser = UserMapper.mapToUser(user);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());

    }

    @Test
    public void registerUser_invalid() throws Exception {
        UserDto user = UserDto.builder()
                .name("John Doe")
                .password("john121")
                .build();
        User newUser = UserMapper.mapToUser(user);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void registerUser_dataMismatch() throws Exception {
        AuthDto user = AuthDto.builder()
                .email("johndoe@gmail.com")
                .password("john121").build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void registerUser_failure() throws Exception {
        UserDto user = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        given(userService.createUser(user)).willThrow(new ItemExistsException("User is already register with email " + user.getEmail()));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isConflict());

    }

    @Test
    public void loginUserTestCase() throws Exception {
        AuthDto user = AuthDto.builder()
                .email("johndoe@gmail.com")
                .password("john121").build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void registerUserTestCase_serverError() throws Exception {
        UserDto user = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        given(userService.createUser(Mockito.any())).willThrow(new RuntimeException());

        mockMvc.perform(mockRequest)
                .andExpect(status().is5xxServerError());
    }
}
