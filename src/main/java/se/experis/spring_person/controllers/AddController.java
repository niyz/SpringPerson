package se.experis.spring_person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import java.util.ArrayList;

@RestController
public class AddController {
    @GetMapping("/add")
    public void addPerson(){
        System.out.println("add metod");
    }

    @GetMapping("/search")
    public ArrayList<Person> searchShit(@RequestParam(name="str") String str){
        //DataBase db = new DataBase();
        return new DataBase().dbSearch(str);
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Person p) {
        System.out.println(p);
        return "result";
    }

}
