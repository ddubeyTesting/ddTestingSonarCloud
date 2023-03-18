package com.zemoso.figma.growthcapital.service.impl;

import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.mapper.UserCreditDetailsMapper;
import com.zemoso.figma.growthcapital.mapper.UserMapper;
import com.zemoso.figma.growthcapital.repository.UserRepository;
import com.zemoso.figma.growthcapital.service.CashkickService;
import com.zemoso.figma.growthcapital.service.ContractService;
import com.zemoso.figma.growthcapital.service.CreditService;
import com.zemoso.figma.growthcapital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private ContractService contractService;

    @Autowired
    @Lazy
    private CashkickService cashkickService;

    @Autowired
    @Lazy
    private CreditService creditService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for the id:"+userId));
    }

    @Override
    public User updateUser(UserDto userDto, Long id) throws ResourceNotFoundException {
        User existingUser = getUserById(id);
        existingUser.setName(userDto.getName() != null ? userDto.getName() : existingUser.getName());
        existingUser.setEmail(userDto.getEmail() != null ? userDto.getEmail() : existingUser.getEmail());
        existingUser.setPassword(userDto.getPassword() != null ? bcryptEncoder.encode(userDto.getPassword()) : existingUser.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUserById(Long userId) throws ResourceNotFoundException {
        User existingUser = getUserById(userId);
        userRepository.delete(existingUser);
    }

    @Override
    public User createUser(UserDto userDto) throws ItemExistsException {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(userDto.getEmail()))) {
            throw new ItemExistsException("User is already register with email:"+userDto.getEmail());
        }
        User newUser = UserMapper.mapToUser(userDto);
        newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public List<Contract> getUserContracts(Long userId) throws ResourceNotFoundException {
        List<Cashkick> userCashkicks =  cashkickService.getUserCashKicks(userId);
        Set<Contract> userContracts = new HashSet<>();
        for(Cashkick cashkick: userCashkicks) {
            userContracts.addAll(cashkick.getContracts());
        }
        return userContracts.stream().toList();
    }

    @Override
    public List<Cashkick> getUserCashkicks(Long userId) throws ResourceNotFoundException {
       return cashkickService.getUserCashKicks(userId);
    }

    @Override
    public UserCreditDetails createUserCreditDetails(UserCreditDetailsDto userCreditDetailsDto, Long userId) throws ResourceNotFoundException {
        UserCreditDetails userCreditDetails = UserCreditDetailsMapper.mapToUserCreditDetails(userCreditDetailsDto);
        userCreditDetails.setUser(getUserById(userId));
        return creditService.createUserCreditDetails(userCreditDetails);
    }

    @Override
    public UserCreditDetails updateUserCreditDetails(UserCreditDetailsDto userCreditDetailsDto, Long userId) throws ResourceNotFoundException {
        UserCreditDetails userCreditDetails = creditService.getUserCredentialsById(userId);
        userCreditDetails.setUser(getUserById(userId));
        userCreditDetails.setAvailableCredit(userCreditDetailsDto.getAvailableCredit());
        userCreditDetails.setCurrency(userCreditDetailsDto.getCurrency());
        return creditService.createUserCreditDetails(userCreditDetails);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for the email"+email));
    }
}
