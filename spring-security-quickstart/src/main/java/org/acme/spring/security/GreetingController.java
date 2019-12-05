package org.acme.spring.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @Secured("admin")
    @GetMapping
    public String hello() {
        return "hello";
    }
}