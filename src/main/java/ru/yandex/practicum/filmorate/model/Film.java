package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Generated;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotEmpty(message = "Название фильма не может быть пустым.")
    private String name; //не пустой

    @Size(min = 0, max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description; //<200 символов

    private LocalDate releaseDate; // > 28.12.1895

    @Positive (message = "Продолжительность фильма должна быть положительной.")
    private int duration; // длительность фильма в минутах, это стандартная единица измерения в каталогах. >0!

}
