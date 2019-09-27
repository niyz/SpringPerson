package se.experis.spring_person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    @GetMapping("/search")
    public String search(Model model){
    	model.addAttribute("str");
    	DataBase db = new DataBase();
    	
    	ObjectMapper om = new ObjectMapper();
    	String out = "";
    	
    	ArrayList<Person> list = db.dbSearch("");
    	for(Person p:list) {
    		try {
				out+=om.writeValueAsString(p);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
    	
        return out;
    }


}
