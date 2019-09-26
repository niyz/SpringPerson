package se.experis.spring_person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import java.util.ArrayList;

@RestController
public class SearchController {

    /*@GetMapping("/searche/{str}")
    //@ResponseBody
    public ArrayList<Person> searchInDb(@PathVariable("str") String str){
        return new DataBase().dbSearch(str);
    }*/

    // works but dont return anything...
    /**
     * det vi skickar som json görs om till ett person objekt det är objektet som sedan används vid sökningen
     */
    @PostMapping("/search")
    public ArrayList<Person> searchPostInDb(@RequestBody Person pers){
        System.out.println(pers.getName());
        return new DataBase().dbSearch(pers.getName());
    }


}
