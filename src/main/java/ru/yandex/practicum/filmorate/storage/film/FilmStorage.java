package ru.yandex.practicum.filmorate.storage.film;

import java.util.Set;
import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

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
     *
     * @return экземпляр фильма
     */
    Optional<Film> retrieveFilmById(long filmId);

    /**
     * Получаем все фильмы
     * @return список всех значений LinkedHashMap, которая хранит все фильмы
     */
    List<Film> retrieveAllFilms();

    /**
     * Получаем популярные фильмы
     * @param count кол-во фильмов, которое надо вернуть
     * @return отсортированный список фильмов по кол-ву лайков
     */
    List<Film> returnTopFilms(int count);

    /**
     * Получаем популярные фильмы
     * @param count кол-во фильмов, которое надо вернуть
     * @param year год релиза фильма
     * @return отсортированный список фильмов по кол-ву лайков с учетом года
     */
    List<Film> returnTopFilmsByYear(int count, int year);

    /**
     * Получаем популярные фильмы
     * @param count кол-во фильмов, которое надо вернуть
     * @param genreId жанр фильма
     * @return отсортированный список фильмов по кол-ву лайков с учетом жанра
     */
    List<Film> returnTopFilmsByGenre(int count, int genreId);

    /**
     * Получаем популярные фильмы
     * @param count кол-во фильмов, которое надо вернуть
     * @param genreId жанр фильма
     * @param year год релиза фильма
     * @return отсортированный список фильмов по кол-ву лайков с учетом жанра и года
     */
    List<Film> returnTopFilmsByGenreAndYear(int count, int genreId, int year);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
    /**
     * Список фильмов режиссера отсортированных по количеству лайков или году выпуска
     * @param directorId id режиссера
     * @param sortBy сортировка может быть либо likes, либо year
     * @return отсортированный список фильмов
     */
    List<Film> findSortFilmsByDirector(Integer directorId, String sortBy);

    /**
     * Список фильмов который лайкнули оба пользователя
     * @param userId id пользователя
     * @param friendId id друга
     * @return список общих фильмов
     */
    public List<Film> getCommonFilms(long userId, long friendId);

    /**
     * Получаем фильмы через поисковый запрос
     * @param query текст-содержание запроса
     * @param option поиск по имени режиссера или названию фильма как по отдельности, так и одновременно
     * @return список фильмов, название или имя режиссера которых содержит текст из query
     */
    Set<Film> searchFilmsByDirectorOrName(String query, List<String> option);
}
