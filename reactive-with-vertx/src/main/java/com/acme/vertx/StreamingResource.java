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

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;

@Path("/hello")
public class StreamingResource {

    @Inject
    io.vertx.reactivex.core.Vertx vertx;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("{name}/streaming")
    public Publisher<String> greeting(@PathParam("name") String name) {
        // Create a stream of ticks (new tick every 2 seconds)
        Flowable<Long> ticks = vertx.periodicStream(2000).toFlowable();

        // Transform ticks into greeting messages
        Publisher<String> publisher = ticks.map(l -> String.format("Hello %s! (%s)%n", name, new Date()));

        return publisher;
    }
}
