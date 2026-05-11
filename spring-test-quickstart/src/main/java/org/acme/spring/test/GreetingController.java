package org.acme.spring.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    public String hello() {
        return greetingService.greet("Quarkus");
    }

    @GetMapping("/{name}")
    public String hello(@PathVariable(name = "name") String name) {
        return greetingService.greet(name);
    }
}