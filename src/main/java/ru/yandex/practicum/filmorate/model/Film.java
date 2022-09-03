package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class Film {

    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;
    private List<Director> directors;
    private Set<Long> likes = new HashSet<>(); //храним ID пользователей, которые поставили лайки

    public Film(long id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}