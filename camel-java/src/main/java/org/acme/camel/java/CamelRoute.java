package org.acme.camel.java;

import org.apache.camel.builder.RouteBuilder;

public class CamelRoute extends RouteBuilder {

    static final String I_AM_ALIVE_MESSAGE = "I'm alive !";

    @Override
    public void configure() {
        from("timer:keep-alive?period={{camel.timer-route.period}}")
                .id("timer-route-java")
                .setBody(constant(I_AM_ALIVE_MESSAGE))
                .to("log:keep-alive");
    }

}
