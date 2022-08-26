package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long id, Long userId) {
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}
