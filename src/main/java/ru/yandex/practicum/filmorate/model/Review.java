package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "reviewId")
@AllArgsConstructor
public class Review {
    private Long reviewId;
    @NotNull
    @Size(max = 200)
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private Long useful;
}
