package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.PersonDTO;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/people")
@Slf4j
public class PeopleRestController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleRestController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        log.warn("Getting all people");
        return peopleService.findAll().stream().map(Convert::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        log.warn("Getting person by id = {}", id);
        return Convert.convertToPersonDTO(peopleService.findOne(id).orElseThrow(() -> new PersonNotFoundException("Person with id = " + id + " wasn't found!")));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        ExceptionsHandler.handler(bindingResult);

        log.warn("Create new person {}", personDTO);
        peopleService.save(Convert.convertToPerson(personDTO));
        log.warn("New person {} was successfully created", personDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        ExceptionsHandler.handler(bindingResult);

        log.warn("Update person {} in DB", personDTO);
        peopleService.save(Convert.convertToPerson(personDTO));
        log.warn("Person {} was successfully updated", personDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        PersonDTO person = getPerson(id);
        peopleService.delete(id);
        log.warn("Person with id = {} was removed", id);
        return "Person was removed";
    }
}
