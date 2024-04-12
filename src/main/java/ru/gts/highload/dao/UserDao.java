package ru.gts.highload.dao;

import ru.gts.highload.model.UserInfo;
import ru.otus.highload.model.User;

import java.util.List;

/**
 * Слой данных пользователя.
 */
public interface UserDao {

    /**
     * Ищет пользователя по логину.
     *
     * @param login логин
     * @return  пользователь или {@code null}, если не найден
     */
    UserInfo findById(String login);

    /**
     * Добавляет пользователя.
     *
     * @param userInfo  пользователь
     * @return  идентификатор пользователя
     */
    String add(UserInfo userInfo);

    List<User> findByName(String firstName, String lastName);
}
