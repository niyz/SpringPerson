package se.experis.spring_person.controllers;


import org.springframework.web.bind.annotation.*;
import se.experis.spring_person.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import se.experis.spring_person.Greeting;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greetingForm(Model model){
        model.addAttribute("greeting", new Person());

        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        return "result";
    }

   /* @RequestMapping(value="createUser", method = RequestMethod.POST)

    public void createUser(RequestBody Person person){


    }*/
}
