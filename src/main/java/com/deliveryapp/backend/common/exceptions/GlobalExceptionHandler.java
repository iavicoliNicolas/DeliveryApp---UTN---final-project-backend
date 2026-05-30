package com.deliveryapp.backend.common.exceptions;

import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.exception.ProductSearchMissingLocationException;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.user.exception.UserEmailAlreadyRegisteredException;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.error("{} {} {} {}", ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI(), errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage("Error en validacion", ex.getClass().getSimpleName(), request.getRequestURI(), errors));
    }

    @ExceptionHandler({
            ProductSearchMissingLocationException.class, InvalidParameterSortByException.class, UserEmailAlreadyRegisteredException.class
    })
    public ResponseEntity<ErrorMessage> handleBadRequestExceptions(HttpServletRequest request, Exception ex) {
        log.error("{} {} {}", ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

    @ExceptionHandler({
            ProductNotFoundException.class, StoreNotFoundException.class, UserNotFoundException.class, OrderNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleElementNotFoundException(HttpServletRequest request, Exception ex) {
        log.error("{} {} {}", ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

    @ExceptionHandler({
            JwtException.class, AuthenticationException.class
    })
    public ResponseEntity<ErrorMessage> forbidden(HttpServletRequest request, Exception ex) {
        log.error("{} {} {}", ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI());
        return ResponseEntity.status(HttpStatusCode.valueOf(403))
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

}
