package org.acme.kafka.streams.aggregator.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class WeatherStation {

    public int id;
    public String name;

    public WeatherStation(){
    }

    public WeatherStation(int id, String name){
        this.id = id;
        this.name = name;
    }
}
