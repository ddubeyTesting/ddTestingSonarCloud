package com.zemoso.figma.growthcapital.service;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CashkickService {
    public Cashkick createNewCashkick(CashkickDto cashkickDto) throws ResourceNotFoundException;

    public List<Cashkick> getAllCashkicks();

    public Cashkick getCashkickById(Long cashkickId) throws ResourceNotFoundException;

    public Cashkick updateCashkick(CashkickDto cashkickDto, Long id) throws ResourceNotFoundException;

    public void deleteCashkickById(Long cashkickId) throws ResourceNotFoundException;

    public List<Cashkick> getUserCashKicks(Long userId) throws ResourceNotFoundException;
}
