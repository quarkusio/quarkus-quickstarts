package org.acme.lifecycle;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyOtherBean {

    public String hello() {
        return "Hello";
    }

    public String bye() {
        return "Bye Bye";
    }

}
