package com.zemoso.figma.growthcapital.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemExistsException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ItemExistsException(String message) {
        super(message);
    }
}
