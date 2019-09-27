package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import java.util.Collections;
import java.util.Map;

@RestController
public class DeleteController {
    /**
     *  Takes a json body and converts it to a Person object to removes it and all relations in the database
     *  the json must contain the personID
     * @param pers Person object
     * @return success true or false as json
     */
    @PostMapping("/delete")
    public Map<String, Boolean> deleteFromDb(@RequestBody Person pers){
        return Collections.singletonMap("success", new DataBase().deletePerson(pers));
    }

    /**
     *  Takes a json body and converts it to a Person object to remove relationship relations in the database
     *  the json must contain the personID
     * @param pers Person object
     * @return success true or false as json
     */
    @PostMapping("/delete/relation")
    public Map<String, Boolean> deleteRelation(@RequestBody Person pers){
        return Collections.singletonMap("success", new DataBase().deleteRelations(pers));
    }
}
