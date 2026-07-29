package com.example.newshowcase.features.testing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomePageController {

    @GetMapping
    public String getWelcomePage() {
        return "<h1>This is a welcome page, please run some request</h1>";
    }
}
