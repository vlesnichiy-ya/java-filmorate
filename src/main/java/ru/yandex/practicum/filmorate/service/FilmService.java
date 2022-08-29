package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private static final LocalDate FILM_BEGIN_DATE = LocalDate.of(1895, 12, 28);

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        validateReleaseDate(film);
        film = filmStorage.addFilm(film);
        log.info("В коллекцию добавлен фильм: {}", film);
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(Long id) {
        validateFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film);
        validateFilmId(film.getId());

        film = filmStorage.updateFilm(film);
        log.info("В коллекции обновлен фильм. Теперь он такой: {}", film);
        return film;
    }


    public void addLike(Long id, Long userId) {
        validateFilmId(id);
        validateUserId(userId);

        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        validateFilmId(id);
        validateUserId(userId);

        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    private boolean validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BEGIN_DATE)) {
            log.error("Слишком ранняя дата релиза фильма {}", film);
            throw new ValidationException("Слишком ранняя дата релиза фильма");
        }
        return true;
    }

    public void validateUserId(Long id) {
        if (!userStorage.isUserExists(id)) {
            log.error("Пользователь c id={} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден", id));
        }
    }


    private void validateFilmId(Long id) {
        if (!filmStorage.checkFilmId(id)) {
            log.error("Фильм c id={} не найден", id);
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", id));
        }
    }
}
