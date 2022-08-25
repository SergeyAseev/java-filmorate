package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {

    private DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> findAll () {
        log.info("Получен запрос к эндпоинту GET, http://localhost:8080/directors");
        return directorService.findAll();
    }

    @GetMapping("/{id}")
    public Director findDirectorById (@PathVariable Integer id) {
        log.info("Получен запрос к эндпоинту GET, http://localhost:8080/directors/{id}");
        return directorService.findDirectorById(id);
    }

    @PostMapping
    public Director createDirector (@Valid @RequestBody Director director) {
        log.info("Получен запрос к эндпоинту POST, http://localhost:8080/directors");
        return directorService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector (@Valid @RequestBody Director director) {
        log.info("Получен запрос к эндпоинту PUT, http://localhost:8080/directors");
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector (@PathVariable Integer id) {
        log.info("Получен запрос к эндпоинту DELETE, http://localhost:8080/directors/{id}");
        directorService.removeDirector(id);
    }
}
