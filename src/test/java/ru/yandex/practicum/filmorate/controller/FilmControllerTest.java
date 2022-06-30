package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

        @Test
        void validateEmptyNameTest() {
                final Film film = new Film(0, "", "testDescription",
                        LocalDate.of(2022,1,1),30);
                final FilmController filmController = new FilmController();
                assertThrows(ValidationException.class, () -> filmController.validate(film));
        }

        @Test
        void validateDescriptionsLengthTest() {
                final Film film = new Film(0, "testName",
                        "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                                "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им " +
                                "деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия»," +
                                " стал кандидатом Коломбани.",
                        LocalDate.of(2022,1,1),30);
                final FilmController filmController = new FilmController();
                assertThrows(ValidationException.class, () -> filmController.validate(film));
        }

        @Test
        void validateReleaseWrongDateTest() {
                final Film film = new Film(0, "testName", "testDescription",
                        LocalDate.of(0,1,1),30);
                final FilmController filmController = new FilmController();
                assertThrows(ValidationException.class, () -> filmController.validate(film));
        }

        @Test
        void validateNegativeDurationTest() {
                final Film film = new Film(0, "testName", "testDescription",
                        LocalDate.of(2022,1,1),-30);
                final FilmController filmController = new FilmController();
                assertThrows(ValidationException.class, () -> filmController.validate(film));
        }

}

