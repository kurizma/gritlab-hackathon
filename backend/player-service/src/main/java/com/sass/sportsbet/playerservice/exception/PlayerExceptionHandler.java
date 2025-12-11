
package com.sass.sportsbet.playerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sass.sportsbet.playerservice.response.ErrorResponse;

@RestControllerAdvice
public class PlayerExceptionHandler {

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePlayerExists(PlayerAlreadyExistsException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .code("PLAYER_ALREADY_EXISTS")
                .message(ex.getMessage())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // 409
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFound(PlayerNotFoundException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .code("PLAYER_NOT_FOUND")
                .message(ex.getMessage())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); // 404
    }
}
