package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.model.Person;
import se.experis.spring_person.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


@RestController
public class AddController {

    /**
     * Takes a json body and converts it to a Person object to add a person to the database
     * @param pers ojbect generated from json
     * @return success true or false as json
     */
    @PostMapping("/add")
    public Map<String, Boolean> addPerson(@RequestBody Person pers){
        return Collections.singletonMap("success", new DataBase().insertPerson(pers));
    }

    /**
     * Takes a json body and converts it to a Person object to add person's relationships to the database
     * @param pers ojbect generated from json
     * @return success true or false as json
     */
    @PostMapping("/add/relation")
    public Map<String, Boolean> addRelation(@RequestBody ArrayList<Person> pers){
        return Collections.singletonMap("success", new DataBase().addRelation(pers.get(0), pers.get(1)));
    }
}
