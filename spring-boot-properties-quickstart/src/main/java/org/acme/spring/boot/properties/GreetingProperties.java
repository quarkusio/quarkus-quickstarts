package org.acme.spring.boot.properties;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("greeting")
public class GreetingProperties {

    public Message message;

    public Optional<String> name;

    public static class Message {

        public String text;

        public String suffix = "!";
    }
}
