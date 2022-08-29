package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> getUsers();

    public User getUser(long id);

    public User addUser(User user);

    public User updateUser(User user);

    public void addFriend(Long userId, Long friendId);

    public List<User> getFriends(Long userId);

    public List<User> getCommonFriends(Long userId, Long otherUserId);

    public void deleteFriend(Long userId, Long friendId);

    public boolean isUserExists(Long id);

    public boolean isLoginValid(User user);
}
