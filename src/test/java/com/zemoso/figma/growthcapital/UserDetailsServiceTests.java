package com.zemoso.figma.growthcapital;

import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.UserRepository;
import com.zemoso.figma.growthcapital.security.CustomUserDetailsService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsService userService = new CustomUserDetailsService();

    @Test
    public void testLoadUserByEmail_success() {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));
        Assertions.assertEquals("johndoe@gmail.com", userService.loadUserByUsername(userDto.getEmail()).getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByEmail_fail() {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .password("john121")
                .build();
        when(userRepository.findByEmail(Mockito.any())).thenThrow(new UsernameNotFoundException("User not found for the email:" + userDto.getEmail()));
        userService.loadUserByUsername(userDto.getEmail());
        verify(userService, times(1)).loadUserByUsername(userDto.getEmail());
    }
}
