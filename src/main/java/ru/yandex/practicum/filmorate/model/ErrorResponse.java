package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    String error;

    private final HttpStatus httpStatus;
    public  ErrorResponse (HttpStatus httpStatus, String error) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

}
