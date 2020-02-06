package org.acme.cache;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeWeatherForecastResourceIT extends WeatherForecastResourceTest {

    // Execute the same tests but in native mode.
}
