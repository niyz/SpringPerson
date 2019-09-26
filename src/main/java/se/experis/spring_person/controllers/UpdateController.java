package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.experis.spring_person.model.DataBase;
import se.experis.spring_person.model.Person;

import javax.xml.ws.Holder;

@RestController
public class UpdateController {

    @PostMapping("/update")
    public void updatePerson(@RequestBody Person oldPers,@RequestBody Person newPers){
        //oldPers.personToString();
        //newPers.personToString();
        System.out.println("old name : " +oldPers.getName());
        System.out.println("new name: " + newPers.getLastName());
        //new DataBase().updatePerson(oldPers, newPers);
    }
    @PostMapping("/test")
    public void test(@RequestBody Holder persHolder){
        System.out.println(persHolder.toString());
    }
}
