package se.experis.spring_person.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import se.experis.spring_person.model.*;

import java.util.ArrayList;

@RestController
public class SearchController {


    @GetMapping("/search")
    public ArrayList<Person> search(Model model){
        DataBase db = new DataBase();

        return db.dbSearch("");
    }

}
