package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int userID = 0;
    private Map<Long, User> store = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return store.values().stream().collect(Collectors.toList());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        validateLogin(user);

        if (user.getName() == "" || user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(generateID());
        store.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        validateLogin(user);
        if (store.containsKey(user.getId())) {
            if (user.getName() == "" || user.getName() == null) {
                user.setName(user.getLogin());
            }
            store.put(user.getId(), user);
            log.info("Пользователь обновлен. Теперь он такой: {}", user);
            return user;
        } else {
            log.error("Пользователь для обновления не найден {}", user);
            throw new ValidationException("Пользователь для обновления не найден");
        }
    }


    private int generateID() {
        return ++userID;
    }


    private boolean validateLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Имя пользователя не может содержать пробелы");
            throw new ValidationException("Имя пользователя не может содержать пробелы");
        }
        return true;
    }

}
