package se.experis.spring_person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import java.util.ArrayList;

@RestController
public class SearchController {


    /**
     * Takes a json body and converts it to a Person object to perform database searches
     * @param pers ojbect generated from json
     * @return arraylist with search results
     */
    @PostMapping("/search")
    public ArrayList<Person> searchPostInDb(@RequestBody Person pers){
        if(pers.getName() != null){
            return new DataBase().dbSearch(pers.getName());
        }
        if(pers.getLastName() != null){
            return new DataBase().dbSearch(pers.getLastName());
        }
        if(pers.getPhoneIDList() != null){
            return new DataBase().dbSearch(pers.getPhoneIDList().get(0));
        }
        return new ArrayList<>();
    }


}
