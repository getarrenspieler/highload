package ru.gts.highload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gts.highload.dao.UserDao;
import ru.gts.highload.model.UserInfo;
import ru.otus.highload.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    public UserInfo get(String id) {
        return userDao.findById(id);

    }

    /**
     * Создаёт пользователя.
     *
     * @param userInfo  пользователь
     * @return  идентификатор пользователя
     */
    public String createUser(UserInfo userInfo) {
        return userDao.add(userInfo);
    }

    public List<User> findByName(String firstName, String lastName) {
        return userDao.findByName(firstName, lastName);
    }
}
