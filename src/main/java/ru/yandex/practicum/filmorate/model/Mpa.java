package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Mpa {

    private int id;
    @NotBlank
    private String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
