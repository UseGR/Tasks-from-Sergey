package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.PersonDTO;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.PersonErrorResponse;
import ru.galeev.springcourse.util.PersonNotCreatedException;
import ru.galeev.springcourse.util.PersonNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/people")
@Slf4j
public class PeopleRestController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleRestController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        log.warn("Getting all people");
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        log.warn("Getting person by ID");
        return convertToPersonDTO(peopleService.findOne(id).orElseThrow(PersonNotFoundException::new));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        handler(bindingResult);

        log.warn("Create new person {}", personDTO);
        peopleService.save(convertToPerson(personDTO));
        log.warn("New person {} is successfully created", personDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        handler(bindingResult);

        log.warn("Update person {} in DB", personDTO);
        peopleService.save(convertToPerson(personDTO));
        log.warn("Person {} is successfully updated", personDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void handler(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";\n");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        PersonDTO person = getPerson(id);
        peopleService.delete(id);
        log.warn("Person with id = {} was removed", id);
        return "Person was removed";
    }


    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> globalExceptionHandler(RuntimeException e) {
        PersonErrorResponse response = null;
        if (e instanceof PersonNotFoundException) {
            response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());
            log.error("Person isn't found");
        } else if (e instanceof PersonNotCreatedException) {
            response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());
            log.error("Bad request");
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
