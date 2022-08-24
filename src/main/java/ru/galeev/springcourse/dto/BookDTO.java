package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.galeev.springcourse.models.Person;

import java.util.Calendar;

@Getter
@Setter
@ToString(of = {"id", "name"})
public class BookDTO {
    private int id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Person person;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Calendar created;
}
