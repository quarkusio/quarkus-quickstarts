package org.acme.quartz;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/tasks")
public class TaskResource {

    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
