package com.zemoso.figma.growthcapital.util;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorObject {

    private Integer statusCode;

    private String message;

    private Date timestamp;
}
