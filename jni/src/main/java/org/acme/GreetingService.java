package org.acme;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String greeting(String name) {
        return "hello " + name;
    }

    public String hello() {
        JniWrapper jni = new JniWrapper();
        return jni.getString();
    }

}
