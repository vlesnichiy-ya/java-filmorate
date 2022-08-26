package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int userID;
    private Map<Long, User> store;

    public InMemoryUserStorage() {
        userID = 0;
        store = new HashMap<>();
    }

    @Override
    public List<User> getUsers() {
        return store.values().stream().collect(Collectors.toList());
    }

    @Override
    public User addUser(User user) throws ValidationException {
        validateLogin(user);

        if (user.getName() == "" || user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(generateID());
        store.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);

        return user;
    }

    @Override
    public User getUser(long id) {
        validateUserId(id);

        return store.get(id);
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        validateLogin(user);
        validateUserId(user.getId());

        if (user.getName() == "" || user.getName() == null) {
            user.setName(user.getLogin());
        }
        store.put(user.getId(), user);
        log.info("Пользователь обновлен. Теперь он такой: {}", user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        store.get(userId).addFriend(friendId);
        store.get(friendId).addFriend(userId); // Зеркальное добавление друга
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        store.get(userId).deleteFriend(friendId);
        store.get(friendId).deleteFriend(userId); // Зеркальное удаление друга
    }

    @Override
    public List<User> getFriends(Long userId) {

        validateUserId(userId);
        Set<Long> friendsId = store.get(userId).getFriends();
        Map<Long, User> subStore = store.entrySet().stream()
                .filter(x -> friendsId.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return subStore.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        validateUserId(userId);
        validateUserId(otherUserId);

        Set<Long> commonFriends = store.get(userId).getFriends().stream()
                .filter(store.get(otherUserId).getFriends()::contains)
                .collect(Collectors.toSet());

        Map<Long, User> subStore = store.entrySet().stream()
                .filter(x -> commonFriends.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return subStore.values().stream().collect(Collectors.toList());
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

    @Override
    public void validateUserId(Long id) {
        if (!store.containsKey(id)) {
            log.error("Пользователь c id={} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден", id));
        }
    }

}
