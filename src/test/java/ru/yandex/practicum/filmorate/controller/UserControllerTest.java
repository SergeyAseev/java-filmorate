package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void validateBirthdayTest() {
        final User user = new User();
        final UserController userController = new UserController();
        user.setBirthday(LocalDate.MAX);
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateEmailTest() {
        final User user = new User(0,"badEmailtTest","testLogin","testName",
                LocalDate.of(2022,1,1));
        final UserController userController = new UserController();
        assertThrows(ValidationException.class, () -> userController.validate(user));

    }

    @Test
    void validateLoginTest() {
        final User user = new User(0,"badEmailtTest"," ","testName",
                LocalDate.of(2022,1,1));
        final UserController userController = new UserController();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateEmptyNameTest() {
        final User user = new User(0,"badEmailtTest","testLogin","",
                LocalDate.of(2022,1,1));
        final UserController userController = new UserController();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }
}
