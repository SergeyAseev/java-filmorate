package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {

    /**
     * Список фильмов режиссера отсортированных по количеству лайков или году выпуска
     * @param directorId id режиссера
     * @param sortBy сортировка может быть либо likes, либо year
     * @return отсортированный список фильмов
     */
    List<Film> findSortFilmsByDirector(Integer directorId, String sortBy);

    /**
     * Проставляем лайк фильму от пользователя
     */
    void addLike(long filmId, long userId);

    /**
     * Удаляем лайк у фильма от пользователя
     */
    void removeLike(long filmId, long userId);

    /**
     * Создаем новый фильм
     * @param film экземпляр текущего фильма в виде Json
     * @return экземпляр созданного фильма
     */
    Film addFilm(Film film);

    /**
     * Обновляем данные уже существующего фильма
     * @param film экземпляр текущего фильма
     * @return экземпляр обновленного фильма
     */
    Film updateFilm(Film film);

    /**
     * Удаляем фильм по ID
     */
    void removeFilmById(long filmId);

    /**
     * Получаем фильм по ID
     * @return экземпляр фильма
     */
    Film retrieveFilmById(long filmId);

    /**
     * Получаем все фильмы
     * @return список значений, который хранит все фильмы
     */
    List<Film> retrieveAllFilms();

    /**
     * Возвращаем отсортированный список фильмов по рейтингу
     * @param count число фильмов, которые надо вернуть
     * @param genreId жанр фильма, который ищем
     * @param year год, за который ищем фильм
     * @return отсортированный список фильмов
     */
    List<Film> returnPopularFilms(int count, int genreId, int year);

    /**
     * Получаем фильмы, которые лайкнули указанные пользователи
     * @return список общих фильмов
     */
     List<Film> getCommonFilms(long userId, long friendId);

     * Получаем фильмы через поисковый запрос
     * @param query текст-содержание запроса
     * @param option поиск по имени режиссера или названию фильма как по отдельности, так и одновременно
     * @return список фильмов, название или имя режиссера которых содержит текст из query
     */
    Set<Film> searchFilmsByDirectorOrName(String query, List<String> option);
}
