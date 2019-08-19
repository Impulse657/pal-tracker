package io.pivotal.pal.tracker.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping("/dumbstuff/{textthing}")
    public String returnStringThing(@PathVariable(value = "textthing") String text) {
        return "this is your text: " + text;
    }
}
