package com.takehome.stayease.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;


public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

