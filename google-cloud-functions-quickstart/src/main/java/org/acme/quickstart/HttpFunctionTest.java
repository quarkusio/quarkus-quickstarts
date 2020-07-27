package org.acme.quickstart;


import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.acme.quickstart.service.GreetingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Writer;

@Named("httpFunction")
@ApplicationScoped
public class HttpFunctionTest implements HttpFunction {
    @Inject
    GreetingService greetingService;

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        Writer writer = httpResponse.getWriter();
        writer.write(greetingService.hello());
    }
}