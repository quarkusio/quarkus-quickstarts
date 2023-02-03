package org.acme.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyOtherBean {

    public String hello() {
        return "hello";
    }

    public String bye() {
        return "bye bye";
    }

}