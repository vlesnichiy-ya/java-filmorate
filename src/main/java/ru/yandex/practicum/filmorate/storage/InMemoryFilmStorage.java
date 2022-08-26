package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate FILM_BEGIN_DATE = LocalDate.of(1895, 12, 28);
    private int filmNextID;
    private Map<Long, Film> store;
    private UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        filmNextID = 0;
        store = new HashMap<>();
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validateReleaseDate(film);
        film.setId(generateID());
        store.put(film.getId(), film);
        log.info("В коллекцию добавлен фильм: {}", film);
        return film;
    }


    @Override
    public List<Film> getFilms() {
        return store.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Long id) {
        validateFilmId(id);

        return store.get(id);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        validateReleaseDate(film);
        validateFilmId(film.getId());

        store.put(film.getId(), film);
        log.info("В коллекции обновлен фильм. Теперь он такой: {}", film);
        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
        validateFilmId(id);
        userStorage.validateUserId(userId);
        store.get(id).addLike(userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        Comparator<Film> comparator = (f1, f2) -> f1.getRating() - f2.getRating();

        return store.values().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        validateFilmId(id);
        userStorage.validateUserId(userId);
        store.get(id).deleteLike(userId);
    }

    private int generateID() {
        return ++filmNextID;
    }


    private boolean validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BEGIN_DATE)) {
            log.error("Слишком ранняя дата релиза фильма {}", film);
            throw new ValidationException("Слишком ранняя дата релиза фильма");
        }
        return true;
    }


    private void validateFilmId(Long id) {
        if (!store.containsKey(id)) {
            log.error("Фильм c id={} не найден", id);
            throw new FilmNotFoundException(String.format("Фильм с id=%s не найден", id));
        }
    }
}
