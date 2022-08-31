package ru.yandex.practicum.filmorate.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidateException
            (final ValidationException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message",
                e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFoundException
            (final NotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message",
                e.getMessage()),HttpStatus.NOT_FOUND);
    }
}
