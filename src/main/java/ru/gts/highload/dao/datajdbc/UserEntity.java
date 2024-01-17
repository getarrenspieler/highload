package ru.gts.highload.dao.datajdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("user_info")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Persistable<String> {

    @Id
    @Column("id")
    private String id;

    @Column("first_name")
    private String firstName;

    @Column("second_name")
    private String secondName;

    @Column("birthdate")
    private LocalDate birthdate;

    @Column("biography")
    private String biography;

    @Column("city")
    private String city;

    @Column("password")
    private String password;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
