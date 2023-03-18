package com.zemoso.figma.growthcapital.service;

import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();

    public User getUserById(Long userId) throws ResourceNotFoundException;

    public User updateUser(UserDto userDto, Long id) throws ResourceNotFoundException;

    public void deleteUserById(Long userId) throws ResourceNotFoundException;

    public User createUser(UserDto userDto) throws ItemExistsException;

    public User getLoggedInUser();

    public List<Contract> getUserContracts(Long userId) throws ResourceNotFoundException;

    public List<Cashkick> getUserCashkicks(Long userId) throws ResourceNotFoundException;

    public UserCreditDetails createUserCreditDetails(UserCreditDetailsDto userCreditDetailsDto, Long userId) throws ResourceNotFoundException;

    public UserCreditDetails updateUserCreditDetails(UserCreditDetailsDto userCreditDetailsDto, Long userId) throws ResourceNotFoundException;
}
