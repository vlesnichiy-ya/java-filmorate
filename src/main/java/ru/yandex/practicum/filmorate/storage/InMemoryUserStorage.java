package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (user.getName() == "" || user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(generateID());
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(long id) {
        return store.get(id);
    }

    @Override
    public User updateUser(User user) throws ValidationException {

        if (user.getName() == "" || user.getName() == null) {
            user.setName(user.getLogin());
        }
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        store.get(userId).addFriend(friendId);
        store.get(friendId).addFriend(userId); // Зеркальное добавление друга
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        store.get(userId).deleteFriend(friendId);
        store.get(friendId).deleteFriend(userId); // Зеркальное удаление друга
    }

    @Override
    public List<User> getFriends(Long userId) {

        Set<Long> friendsId = store.get(userId).getFriends();
        Map<Long, User> subStore = store.entrySet().stream()
                .filter(x -> friendsId.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return subStore.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {

        Set<Long> commonFriends = store.get(userId).getFriends().stream()
                .filter(store.get(otherUserId).getFriends()::contains)
                .collect(Collectors.toSet());

        Map<Long, User> subStore = store.entrySet().stream()
                .filter(x -> commonFriends.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return subStore.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean isLoginValid(User user) {
        if (user.getLogin().contains(" ")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isUserExists(Long id) {
        return store.containsKey(id);
    }

    private int generateID() {
        return ++userID;
    }


}
