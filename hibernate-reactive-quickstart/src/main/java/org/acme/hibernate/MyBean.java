package org.acme.hibernate;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class MyBean {
    
    int count = 0;

    public int incrAndGet() {
        return count++;
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Oh no! I'm going to be destroyed");
    }

}
