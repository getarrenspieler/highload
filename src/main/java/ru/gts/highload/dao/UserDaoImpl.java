package ru.gts.highload.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gts.highload.dao.datajdbc.UserEntity;
import ru.gts.highload.dao.datajdbc.UserRepository;
import ru.gts.highload.model.UserInfo;
import ru.otus.highload.model.User;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    @Override
    public UserInfo findById(String id) {
        return userRepository.findById(id).map(UserDaoImpl::toUserInfo).orElse(null);
    }

    @Override
    public String add(UserInfo userInfo) {
        UserEntity userEntity = fromUserInfo(userInfo);
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setNew(true);
        userEntity = userRepository.save(userEntity);
        return userEntity.getId();
    }

    @Override
    public List<User> findByName(String firstName, String lastName) {
        return userRepository.findAllByFirstNameStartsWithAndSecondNameStartsWith(firstName, lastName)
                .stream()
                .map(UserDaoImpl::toUser)
                .collect(toList());
//        return userRepository.findAllByName(
//                defaultIfBlank(firstName, "") + "%",
//                defaultIfBlank(lastName, "") + "%")
//                .stream()
//                .map(UserDaoImpl::toUser)
//                .collect(toList());
    }

    private static UserInfo toUserInfo(UserEntity entity) {
        return UserInfo.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .biography(entity.getBiography())
                .city(entity.getCity())
                .birthdate(entity.getBirthdate())
                .password(entity.getPassword())
                .build();
    }

    private static User toUser(UserEntity entity) {
        return new User()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .biography(entity.getBiography())
                .city(entity.getCity())
                .birthdate(entity.getBirthdate());
    }

    private static UserEntity fromUserInfo(UserInfo userInfo) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userEntity.getId());
        userEntity.setFirstName(userInfo.getFirstName());
        userEntity.setSecondName(userInfo.getSecondName());
        userEntity.setCity(userInfo.getCity());
        userEntity.setBiography(userInfo.getBiography());
        userEntity.setBirthdate(userInfo.getBirthdate());
        userEntity.setPassword(userInfo.getPassword());
        return userEntity;
    }
}
