package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.galeev.springcourse.models.Person;

@Getter
@Setter
@ToString(of = {"id", "name", "createdDate"})
public class BookDTO {
    private int id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Person person;

    private String name;

    private int createdDate;

}
