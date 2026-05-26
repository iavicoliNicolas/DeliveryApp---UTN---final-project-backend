package com.deliveryapp.backend.common.exceptions;

import com.deliveryapp.backend.order.exception.OrderNotFoundException;
import com.deliveryapp.backend.product.exception.InvalidParameterSortByException;
import com.deliveryapp.backend.product.exception.ProductNotFoundException;
import com.deliveryapp.backend.product.exception.ProductSearchMissingLocationException;
import com.deliveryapp.backend.store.exception.StoreNotFoundException;
import com.deliveryapp.backend.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage("Error en validacion", ex.getClass().getSimpleName(), request.getRequestURI(), errors));
    }

    @ExceptionHandler({
            ProductSearchMissingLocationException.class, InvalidParameterSortByException.class
    })
    public ResponseEntity<ErrorMessage> handleBadRequestExceptions(HttpServletRequest request, Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

    @ExceptionHandler({
            ProductNotFoundException.class, StoreNotFoundException.class, UserNotFoundException.class, OrderNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleElementNotFoundException(HttpServletRequest request, Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

    @ExceptionHandler({
            AuthenticationException.class
    })
    public ResponseEntity<ErrorMessage> handleAuthenticationExceptions(HttpServletRequest request, Exception ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName(), request.getRequestURI()));
    }

    /**
     @ExceptionHandler(OrderException.class) public ResponseEntity<ErrorMessage> handleOrderException(HttpServletRequest request, OrderException ex) {
     return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(...));
     //409
     }
     **/

}
