package ru.galeev.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        log.info("Method findAll is returning all people...");
        return peopleRepository.findAll();
    }

    public Optional<Person> findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        log.info("Method findOne is returning person with id = {}...", id);
        return foundPerson;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
        log.info("Method save is keeping person {}...", person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
        log.info("Method update is updating person {} with id = {}...", updatedPerson, id);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
        log.info("Method delete is removing person with id = {}...", id);
    }

    public boolean exists(int id) {
        log.info("method exists is checking person with id = {}...", id);
        return peopleRepository.existsById(id);
    }
}
