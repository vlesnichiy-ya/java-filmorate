package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate FILM_BEGIN_DATE = LocalDate.of(1895, 12, 28);
    private int filmID = 0;
    private HashMap<Integer, Film> store = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return store.values().stream().collect(Collectors.toList());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FILM_BEGIN_DATE)) {
            log.error("Слишком ранняя дата релиза фильма {}", film);
            throw new ValidationException("Слишком ранняя дата релиза фильма");
        }

        film.setId(generateID());
        store.put(film.getId(), film);
        log.debug("В коллекцию добавлен фильм: {}", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FILM_BEGIN_DATE)) {
            log.error("Слишком ранняя дата релиза фильма {}", film);
            throw new ValidationException("Слишком ранняя дата релиза фильма");
        }
        if (store.containsKey(film.getId())) {
            store.put(film.getId(), film);
            log.debug("В коллекции обновлен фильм. Теперь он такой: {}", film);
            return film;
        } else {
            log.error("Фильм для обновления не найден {}", film);
            throw new ValidationException("Фильм для обновления не найден");
        }
    }


    private int generateID() {
        return ++filmID;
    }

}
