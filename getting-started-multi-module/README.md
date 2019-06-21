# Quarkus demo: multi-module

This example showcases the use of Quarkus dev mode with a multi-module application.

# Run the demo

- Run `mvn clean install` or  `./gradlew build`
- Start dev mode `mvn quarkus:dev -pl getting-started-app` or  `./gradlew getting-started-app:quarkusDev`
- Open your browser to `http://localhost:8080/hello/greeting/you`, it should display "hello you"
- Do a change in [GreetingService](./getting-started-services/src/main/java/org/acme/quickstart/GreetingService.java)
- Refresh the page `http://localhost:8080/hello/greeting/you` and see your changes reflected

You should see message "Changed source files detected, recompiling [getting-started-multi-module\getting-started-services\src\main\java\org\acme\quickstart\GreetingService.java]" logged to the shell console.
