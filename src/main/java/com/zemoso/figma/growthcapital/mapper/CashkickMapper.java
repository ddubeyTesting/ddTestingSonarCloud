package com.zemoso.figma.growthcapital.mapper;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.util.StatusType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CashkickMapper {
    public static Cashkick mapToCashkick(CashkickDto cashkickDto) {
        Cashkick cashkick = new Cashkick();
        cashkick.setName(cashkickDto.getName());
        cashkick.setStartdate(new Date(System.currentTimeMillis()));
        Instant instant = LocalDate.now().plusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date maturityDate = Date.from(instant);
        cashkick.setMaturity(maturityDate);
        cashkick.setRate(12);
        cashkick.setStatus(StatusType.PENDING.name());
        return cashkick;
    }

    private CashkickMapper() {

    }

}
