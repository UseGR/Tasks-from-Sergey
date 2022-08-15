package ru.galeev.springcourse.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galeev.springcourse.controllers.PeopleRestController;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.repositories.PeopleRepository;
import ru.galeev.springcourse.util.PersonNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private static final Logger LOG = LoggerFactory.getLogger(PeopleService.class);
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        LOG.info("Method findAll returns all entities");
        return peopleRepository.findAll(); //.findAll (возвращает все сущности из БД)
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        LOG.info("Method findOne returns entity by ID");
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
        LOG.info("Method save keeps entity");
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
        LOG.info("Method update updates entity by ID");
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
        LOG.info("Method delete removes entity by ID");
    }
}
