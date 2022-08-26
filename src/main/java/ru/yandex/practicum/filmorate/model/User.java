package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class User {
    private long id = 0;
    @Email(message = "Указан некорректный email.")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым.")
    private String login; //не пустой, не содержит " "

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday; // ДР не может быть в будущем

    private Set<Long> friends = new HashSet<>();

    public void addFriend(long friendId) {
        friends.add(friendId);
    }

    public boolean deleteFriend(long friendId) {
        return friends.remove(friendId);
    }

    public Set<Long> getFriends() {
        return friends;
    }
}
