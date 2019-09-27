package se.experis.spring_person.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import se.experis.spring_person.model.*;


import java.util.ArrayList;

@Controller
public class SearchCon{


    @GetMapping("/searchCon")
    public String search(Model model){
        DataBase db = new DataBase();
        ArrayList<Person> pa = db.dbSearch("");
/*
        ArrayList<String> name = new ArrayList<>();
        for (Person p: pa
             ) {
            String emails = "";
            String phones = "";

            for (String str: p.getEmailList()
                 ) {
                emails = emails + str + ", ";

            }

            for (String str2: p.getPhoneIDList()
            ) {
                phones = phones + str2 + ", ";

            }

            model.addAttribute("name", p.getName());
            model.addAttribute("lastName", p.getLastName());
            model.addAttribute("personID", p.getPersonID());
            model.addAttribute("emailList", emails);
            model.addAttribute("phoneIDList", phones);
            model.addAttribute("address", p.getAddress().addrToString());


        }


        return "searchCon";
*/

        model.addAttribute("people", pa);
        return "searchCon";
    }



    /**
     * Takes a json body and converts it to a Person object to perform database searches
     * @param pers ojbect generated from json
     * @return arraylist with search results
     */



}
