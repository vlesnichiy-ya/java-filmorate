package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Generated;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    @NotEmpty(message = "Название фильма не может быть пустым.")
    private String name; //не пустой

    @Size(min = 0, max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description; //<200 символов

    private LocalDate releaseDate; // > 28.12.1895

    @Positive (message = "Продолжительность фильма должна быть положительной.")
    private int duration; // длительность фильма в минутах, это стандартная единица измерения в каталогах. >0!

    private Set<Long> likes = new HashSet<>();

    private int rating = 0;


    public void addLike(Long userId) {
        likes.add(userId);
        calculateRating();
    }

    public boolean deleteLike(Long userId) {
        boolean result = likes.remove(userId);
        calculateRating();
        return result;
    }

    public int getRating() {
        return rating;
    }

    private void calculateRating() {
        rating = likes.size();
    }
}
