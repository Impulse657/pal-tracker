package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private String welcomeMessage;

    public WelcomeController(@Value("${welcome.message}") String welcomeMessage){

        this.welcomeMessage = welcomeMessage;
    }

    @GetMapping("/")
    public String sayHello() {
        return welcomeMessage;
    }

    @GetMapping("/dumbstuff/{textthing}")
    public String returnStringThing(@PathVariable(value = "textthing") String text) {
        return "this is your text: " + text;
    }
}
