package se.experis.spring_person.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.experis.spring_person.model.Person;
import se.experis.spring_person.model.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@RestController
public class UpdateController {

    /**
     * Takes a json array with person information and updates the first element in the array with the second element
     * pers.get(1) = the new values
     * pers.get(0) = the old values
     * @param pers ojbect generated from json
     * @return success true or false as json
     */
    @PostMapping("/update")
    public Map<String, Boolean> test(@RequestBody ArrayList<Person> pers){

        return Collections.singletonMap("success", new DataBase().updatePerson(pers.get(0), pers.get(1)));
    }

    /* the json must be sent as an array containing the old information first followed by the new information
    [{
        "name": "sven",
        "personID": "197702136543",
        "lastName": "andersson",
        "phoneIDList": [
            "07378987",
            "07278765"
        ],
        "emailList": [
            "sven@svenne.se"
        ],
        "address": {
            "country": "swe",
            "city": "vxo",
            "street": "klo",
            "postalCode": "1234",
            "streetNum": "1"
        }
    },
{
        "name": "sven",
        "personID": "197702136503",
        "lastName": "andersson",
        "phoneIDList": [
            "07378987",
            "07278765"
        ],
        "emailList": [
            "sven@svenne.se"
        ],
        "address": {
            "country": "swe",
            "city": "vxo",
            "street": "klo",
            "postalCode": "1234",
            "streetNum": "1"
        }
    }]
     */
}
