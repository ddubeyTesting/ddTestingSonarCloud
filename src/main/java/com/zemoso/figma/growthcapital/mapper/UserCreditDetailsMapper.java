package com.zemoso.figma.growthcapital.mapper;

import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;

import java.math.BigDecimal;

public class UserCreditDetailsMapper {
    public static UserCreditDetails mapToUserCreditDetails(UserCreditDetailsDto userCreditDetailsDto) {
        UserCreditDetails userCreditDetails = new UserCreditDetails();
        userCreditDetails.setAvailableCredit(userCreditDetailsDto.getAvailableCredit());
        userCreditDetails.setCurrency(userCreditDetailsDto.getCurrency());
        userCreditDetails.setOutstandingAmount(BigDecimal.valueOf(0.0d));
        return userCreditDetails;
    }

    private UserCreditDetailsMapper() {

    }
}
