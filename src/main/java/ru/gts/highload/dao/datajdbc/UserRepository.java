package ru.gts.highload.dao.datajdbc;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    @Override
    Optional<UserEntity> findById(String id);

}
