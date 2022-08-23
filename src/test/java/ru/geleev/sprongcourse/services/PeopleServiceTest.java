package ru.geleev.sprongcourse.services;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.repositories.PeopleRepository;
import ru.galeev.springcourse.services.PeopleService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


public class PeopleServiceTest {
    @Mock
    private PeopleRepository peopleRepository;

    private PeopleService peopleService;

    public PeopleServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.peopleService = new PeopleService(peopleRepository);
    }

    @Test
    public void findAllShouldReturnAllPeople() {
        given(peopleService.findAll()).willReturn(Arrays.asList(
                new Person(1, "Tony", 42, "stark@avengers.com"),
                new Person(2, "Eddie", 30, "brock@venom.com")));
        List<Person> people = peopleService.findAll();

        assertThat(people).contains(new Person(1, "Tony", 42, "stark@avengers.com"));
        assertThat(people).contains(new Person(2, "Eddie", 30, "brock@venom.com"));
        assertThat(people).doesNotContain(new Person(3, "Jack", 35, "sparrow@pirate.com"));

    }

    @Test
    public void findPersonByIdShouldReturnPersonWithId() {
        given(peopleService.findOne(1)).willReturn(Optional.of(new Person(1, "Tony", 42, "stark@avengers.com")));

        Person person = peopleService.findOne(1).orElse(null);
        assertEquals(1, person.getId());
    }

    @Test(expected = NullPointerException.class)
    public void findPersonByIdShouldReturnNull() {
        Person person = peopleService.findOne(1).orElse(null);
        assertEquals(1, person.getId());
    }

    @Test
    public void saveShouldSavePersonById() {
        Person person = new Person(1, "Tony", 42, "stark@avengers.com");
        peopleService.save(person);
        verify(peopleRepository).save(person);
    }

    @Test
    public void updateShouldUpdatePersonById() {
        Person person = new Person(1, "Tony", 42, "stark@avengers.com");
        peopleService.update(1, person);
        verify(peopleRepository).save(person);
    }

    @Test
    public void deleteShouldDeletePersonById(){
        peopleService.delete(1);
        verify(peopleRepository).deleteById(1);
    }

    @Test
    public void existsShouldReturnTrue() {
        given(peopleService.exists(1)).willReturn(true);
        assertTrue(peopleService.exists(1));
    }
}
