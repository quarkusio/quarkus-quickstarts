package org.acme.quickstart;

import io.quarkus.funqy.Funq;

public class GreetingFunqy {

    @Funq
    public String funqy() {
        return "Make it funqy";
    }
}
