package ru.gts.highload.dao;

import ru.gts.highload.model.UserInfo;

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
}
