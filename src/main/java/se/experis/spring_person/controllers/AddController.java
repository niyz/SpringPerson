package se.experis.spring_person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;


@RestController
public class AddController {

    // works but dont return anything...
    @PostMapping("/add")
    public void addPerson(@RequestBody Person pers){
        new DataBase().insertPerson(pers);
    }

    // add relations stuff
}
