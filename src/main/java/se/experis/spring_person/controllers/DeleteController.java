package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

@RestController
public class DeleteController {
    // works but dont return anything...
    @PostMapping("/delete")
    public void deleteFromDb(@RequestBody Person pers){
        new DataBase().deletePerson(pers);
    }

    //delete relation stuff if one would like to just delete relations...
}
