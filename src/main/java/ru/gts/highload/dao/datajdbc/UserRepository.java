package ru.gts.highload.dao.datajdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    @Override
    Optional<UserEntity> findById(String id);

    @Query(value = """
                SELECT * FROM user_info
                WHERE first_name iLIKE :firstName
                    AND second_name iLIKE :lastName
                """)
    List<UserEntity> findAllByName(@Param("firstName") String firstName,
                                   @Param("lastName") String lastName);

    List<UserEntity> findAllByFirstNameStartsWithAndSecondNameStartsWith(String firstName, String lastName);

}
