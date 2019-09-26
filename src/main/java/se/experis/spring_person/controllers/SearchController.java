package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import java.util.ArrayList;

@RestController
public class SearchController {
    @PostMapping("/search")
    public ArrayList<Person> getSearch(@ModelAttribute String str){
        DataBase db = new DataBase();
        ArrayList<Person> arr = db.dbSearch(str);
        for(Person p: arr){
            System.out.println(p);
        }
        return arr;
    }
}
