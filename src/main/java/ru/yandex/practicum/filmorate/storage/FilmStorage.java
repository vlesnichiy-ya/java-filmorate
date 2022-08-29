package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film addFilm(Film film);

    public List<Film> getFilms();

    public Film getFilm(Long id);

    public boolean checkFilmId(Long id);

    public Film updateFilm(Film film);

    public void addLike(Long id, Long userId);

    public List<Film> getTopFilms(int count);

    public void deleteLike(Long id, Long userId);

}
