package ru.gts.highload.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;
import ru.gts.highload.dao.datajdbc.UserEntity;
import ru.gts.highload.dao.datajdbc.UserRepository;
import ru.gts.highload.model.UserInfo;

import java.util.UUID;

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
        UserEntity userEntity = fromUser(userInfo);
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setNew(true);
        userEntity = userRepository.save(userEntity);
        return userEntity.getId();
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

    private static UserEntity fromUser(UserInfo userInfo) {
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
