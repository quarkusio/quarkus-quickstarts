package org.acme.quickstart;

import javax.inject.Singleton;

@Singleton
public class GreetingService {

    public String greeting(String name) {
        return "hello " + name;
    }

}
