package org.acme.quickstart;

import com.google.cloud.functions.Context;
import com.google.cloud.functions.RawBackgroundFunction;
import org.acme.quickstart.service.GreetingService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("rawPubSubTest")
@ApplicationScoped
public class RawBackgroundFunctionPubSubTest implements RawBackgroundFunction {
    @Inject
    GreetingService greetingService;

    @Override
    public void accept(String event, Context context) throws Exception {
        System.out.println("PubSub event: " + event);
        System.out.println("Be polite, say " + greetingService.hello());
    }
}
