package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long filmNextID;
    private Map<Long, Film> store;

    @Autowired
    public InMemoryFilmStorage() {
        filmNextID = 0;
        store = new HashMap<>();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generateID());
        store.put(film.getId(), film);
        return film;
    }


    @Override
    public List<Film> getFilms() {
        return store.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Long id) {
        return store.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        store.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
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
        store.get(id).deleteLike(userId);
    }

    @Override
    public boolean checkFilmId(Long id) {
        return store.containsKey(id);
    }

    private long generateID() {
        return ++filmNextID;
    }
}
