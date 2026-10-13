package org.acme.quarkus.greeting.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.acme.quarkus.greeting.GreetingServlet;
import io.quarkus.undertow.deployment.ServletBuildItem;

class GreetingProcessor {

    private static final String FEATURE = "greeting";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ServletBuildItem createServlet() { // <1>
        ServletBuildItem servletBuildItem = ServletBuildItem.builder("greeting", GreetingServlet.class.getName())
                .addMapping("/greeting")
                .build(); // <2>
        return servletBuildItem;
    }

}