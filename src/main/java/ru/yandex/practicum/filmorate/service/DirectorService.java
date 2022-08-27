package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class DirectorService {

    private DirectorDao directorDao;

    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    public List<Director> findAll () {
        return directorDao.findAll();
    }

    public Director findDirectorById (Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("некоректный id");
        }
        return directorDao.findDirectorById(id);
    }

    public Director createDirector (Director director) {
        if (director.getId() == null || director.getId() <= 0) {
            throw new NotFoundException("некоректный id при создании директора");
        }
        return directorDao.createDirector(director);
    }

    public Director updateDirector (Director director) {
        if (director.getId() == null || director.getId() <= 0) {
            throw new NotFoundException("некоректный id при создании директора");
        }
        return directorDao.updateDirector(director);
    }

    public void removeDirector (Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("некоректный id");
        }
        directorDao.removeDirector(id);
    }
}
