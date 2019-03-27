package org.acme.camel.java;

import org.apache.camel.builder.RouteBuilder;

public class CamelRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:keep-alive?period={{camel.timer-route.period}}")
                .id("timer-route-java")
                .setBody(constant("I'm alive !"))
                .to("log:keep-alive");
    }

}
