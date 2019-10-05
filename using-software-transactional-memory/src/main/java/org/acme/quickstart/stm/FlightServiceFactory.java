package org.acme.quickstart.stm;

import org.jboss.stm.Container;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class FlightServiceFactory {
    private FlightService flightServiceProxy;

    private void initFlightServiceFactory() {
        Container<FlightService> container = new Container<>();
        flightServiceProxy = container.create(new FlightServiceImpl());
    }

    FlightService getInstance() {
        if (flightServiceProxy == null) {
            initFlightServiceFactory();
        }
        return flightServiceProxy;
    }
}
