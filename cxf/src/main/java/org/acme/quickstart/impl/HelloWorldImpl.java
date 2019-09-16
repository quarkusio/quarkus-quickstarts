package org.acme.quickstart.impl;

import javax.jws.WebService;

import org.acme.quickstart.HelloWorld;

@WebService
public class HelloWorldImpl implements HelloWorld {
    /*
     * (non-Javadoc)
     * @see test.IHello#sayHi(java.lang.String)
     */
    public String sayHi(String name) {
        return "Hello " + name;
    }
}