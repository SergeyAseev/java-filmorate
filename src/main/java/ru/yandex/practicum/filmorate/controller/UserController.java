package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> retrieveAllUsers() {
        return userService.retrieveAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/{id}")
    public User retrieveUserBuId(@PathVariable int id) {
        return userService.retrieveUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}")
    public void removeUserById(@PathVariable int id) {
        userService.removeUserById(id);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> retrieveFriends(@PathVariable int id) {
        return userService.retrieveFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> retrieveCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.retrieveCommonFriends(id, otherId);
    }
}
