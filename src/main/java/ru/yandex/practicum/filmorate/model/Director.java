package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Director {

    private Integer id;
    @NonNull
    @NotBlank
    private String name;
}
