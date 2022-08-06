package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private int id = 0;
    @Email (message = "Указан некорректный email.")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым.")
    private String login; //не пустой, не содержит " "

    private String name;

    @Past (message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday; // ДР не может быть в будущем

    public int generateID() {
        return ++id;
    }
}
