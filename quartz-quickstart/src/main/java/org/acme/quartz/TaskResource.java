package org.acme.quartz;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
