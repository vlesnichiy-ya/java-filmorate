package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(long id) {
        validateUserId(id);
        return userStorage.getUser(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        validateLogin(user);
        user = userStorage.addUser(user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        validateLogin(user);
        validateUserId(user.getId());
        user = userStorage.updateUser(user);
        log.info("Пользователь обновлен. Теперь он такой: {}", user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);
        userStorage.addFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        validateUserId(id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);
        return userStorage.getCommonFriends(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    private void validateLogin(User user) {
        if (!userStorage.isLoginValid(user)) {
            log.error("Имя пользователя не может содержать пробелы");
            throw new ValidationException("Имя пользователя не может содержать пробелы");
        }
    }

    private void validateUserId(Long id) {
        if (!userStorage.isUserExists(id)) {
            log.error("Пользователь с id={} не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден.", id));
        }
    }

}
