package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (@Qualifier("UserDbService")UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> retrieveAllUsers() {
        return userService.retrieveAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/{id}")
    public User retrieveUserBuId(@PathVariable long id) {
        return userService.retrieveUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}")
    public void removeUserById(@PathVariable long id) {
        userService.removeUserById(id);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> retrieveFriends(@PathVariable long id) {
        return userService.retrieveFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> retrieveCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.retrieveCommonFriends(id, otherId);
    }
    @GetMapping(value = "/{id}/recommendations")
    public List<Optional<Film>> getRecommendations(@PathVariable long id) {
        return userService.getRecommendations(id);
    }
}
