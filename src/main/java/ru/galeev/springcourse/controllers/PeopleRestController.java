package ru.galeev.springcourse.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.PersonErrorResponse;
import ru.galeev.springcourse.util.PersonNotCreatedException;
import ru.galeev.springcourse.util.PersonNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PeopleRestController {
    private final PeopleService peopleService;
    private static final Logger LOG = LoggerFactory.getLogger(PeopleRestController.class);

    @Autowired
    public PeopleRestController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public List<Person> getPeople() {
        LOG.warn("Getting all people");
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        LOG.warn("Getting person by ID");
        return peopleService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";\n");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }
        LOG.warn("Insert new person in DB");
        peopleService.save(person);
        LOG.warn("New person successfully inserted");

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";\n");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }
        LOG.warn("Updating person in DB");
        peopleService.save(person);
        LOG.warn("Data is updated");

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        Person person = getPerson(id);
        peopleService.delete(id);
        LOG.warn("Person was removed");
        return "Person was removed";
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person isn't found!",
                System.currentTimeMillis()
        );
        LOG.error("Error! Person isn't found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        LOG.error("Bad request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
