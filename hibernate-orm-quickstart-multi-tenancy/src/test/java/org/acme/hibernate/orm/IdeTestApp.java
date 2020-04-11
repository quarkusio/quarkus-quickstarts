package org.acme.hibernate.orm;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Workaround to allow launching the Quarkus application inside the IDE.
 * 
 * @see https://github.com/quarkusio/quarkus/issues/2143
 */
@QuarkusTest
public class IdeTestApp {

    @Test
    public void startApp() throws Exception {
        while (true) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
