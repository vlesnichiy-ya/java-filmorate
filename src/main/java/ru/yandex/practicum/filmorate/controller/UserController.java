package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/users")
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@Positive @PathVariable("id") Long id) {
        return userStorage.getUser(id);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@Positive @PathVariable("id") Long id,
                          @Positive @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@Positive @PathVariable("id") Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@Positive @PathVariable("id") Long id,
                                       @Positive @PathVariable("friendId") Long friendId) {
        return userService.getCommonFriends(id, friendId);
    }


    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@Positive @PathVariable("id") Long id,
                             @Positive @PathVariable("friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

}
