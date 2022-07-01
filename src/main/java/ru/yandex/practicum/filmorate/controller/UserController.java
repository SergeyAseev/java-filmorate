package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    protected int nextId = 0;
    private final Map<Integer, User> users = new LinkedHashMap<Integer, User>();

    /**
     *  Получаем всех пользователей
     * @return список всех значений LinkedHashMap, которая хранит всех пользователей
     */
    @GetMapping("users")
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * Создаем нового пользователя
     * @param user экземпляр текущего пользователя в виде Json
     * @return экземпляр созданного пользователя
     */
    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь c ID {} успешно добавлен", user.getId());
        return user;
    }

    /**
     * Обновляем данные уже созданного пользователя
     * @param user экземпляр текущего пользователя
     * @return экземпляр обновленного пользователя
     */
    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            RuntimeException runtimeException = new ValidationException("Не найден пользователь с таким ID");
            log.info(runtimeException.getMessage());
            throw runtimeException;
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь c ID {} успешно обновлен", user.getId());
        return user;
    }

    /**
     * Проверка создаваемого пользователя на валидность
     * @param user экземпляр текущего пользователя
     */
    protected void validate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже текущей даты");
        }
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email некорретный");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("login не может быть пустым");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    /**
     * увеличивает уникальный идентификатор пользователя
     */
    protected int generateId() {
        return ++nextId;
    }
}
