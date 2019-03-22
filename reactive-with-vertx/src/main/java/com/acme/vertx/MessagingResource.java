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

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.vertx.axle.core.eventbus.Message;

@Path("/hello")
public class MessagingResource {

    @Inject
    io.vertx.axle.core.eventbus.EventBus eventBus;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}/messaging")
    public CompletionStage<String> greeting(@PathParam("name") String name) {
        CompletionStage<Message<String>> greeting = eventBus.send("greeting", name);
        return greeting.thenApply(Message::body);
    }
}
