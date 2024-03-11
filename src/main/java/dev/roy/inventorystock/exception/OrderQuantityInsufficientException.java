package dev.roy.inventorystock.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderQuantityInsufficientException extends RuntimeException{

    public OrderQuantityInsufficientException(String message) {
        super(message);
    }
}

