package org.acme.redis.streams.consumer.util;

public final class AppConstants {

    // TABLES
    public static final String WEATHER_STATIONS_TABLE = "station:";
    public static final String AGGREGATE_TABLE = "aggregates";

    // STREAMS
    public static final String TEMPERATURE_VALUES_STREAM = "temperature-values";

    // CONSUMER-GROUPS
    public static final String CONSUMER_GROUP = "temperature-aggregates-group";
}
