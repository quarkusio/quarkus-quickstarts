package org.acme.funqy;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import org.jboss.logging.Logger;

public class SimpleFunctionChain {
    private static final Logger log = Logger.getLogger(SimpleFunctionChain.class);

    /**
     * Expects knative event of type "defaultChain".  Creates event of type "defaultChain.output".
     *
     * From the guide, this function is triggered by an external curl invocation.
     *
     * @param input
     * @return
     */
    @Funq
    public String defaultChain(String input) {
        log.info("*** defaultChain ***");
        return input + "::" + "defaultChain";
    }

    /**
     * This is triggered by defaultChain and is example of using application.properties to
     * map the cloud event to this function and to map response.  Response will trigger
     * the annotatedChain function.
     *
     * application.properties will have:
     *
     * quarkus.funqy.knative-events.mapping.configChain.trigger=defaultChain.output
     * quarkus.funqy.knative-events.mapping.configChain.response-type=annotated
     * quarkus.funqy.knative-events.mapping.configChain.response-source=configMapping
     *
     *
     *
     * @param input
     * @return
     */
    @Funq
    public String configChain(String input) {
        log.info("*** configChain ***");
        return input + "::" + "configChain";
    }

    /**
     * Triggered by configChain output.
     *
     * Example of mapping the cloud event via an annotation.
     *
     * @param input
     * @return
     */
    @Funq
    @CloudEventMapping(trigger = "annotated", responseSource = "annotated", responseType = "lastChainLink")
    public String annotatedChain(String input) {
        log.info("*** annotatedChain ***");
        return input + "::" + "annotatedChain";
    }

    /**
     * Last event in chain.  Has no output.  Triggered by annotatedChain.
     *
     * @param input
     */
    @Funq
    public void lastChainLink(String input, @Context CloudEvent event) {
        log.info("*** lastChainLink ***");
        log.info(input + "::" + "lastChainLink");
    }
}
