/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.acme.vertx;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.Vertx;

@ApplicationScoped
public class GreetingService {

    @Inject
    Vertx vertx;

    @ConsumeEvent("greeting")
    CompletionStage<String> greet(String name) {
        // When complete, return the content to the sender
        CompletableFuture<String> future = new CompletableFuture<>();

        long start = System.nanoTime();

        // Delay reply by 10ms
        vertx.setTimer(10, l -> {
            // Compute elapsed time in milliseconds
            long duration = MILLISECONDS.convert(System.nanoTime() - start, NANOSECONDS);

            // Format message
            String message = String.format("Hello %s! (%d ms)%n", name, duration);

            // Complete
            future.complete(message);
        });

        return future;
    }
}
