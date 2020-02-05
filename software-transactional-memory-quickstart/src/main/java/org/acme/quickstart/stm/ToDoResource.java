package org.acme.quickstart.stm;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

@Path("/stm")
@RequestScoped
public class ToDoResource {
    ExecutorService executor;

    @ConfigProperty(name = "org.acme.quickstart.stm.threadpool.size")
    int threadPoolSize;

    @Inject
    ToDoFactory factory;

    @PostConstruct
    void postConstruct() {
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    @PreDestroy
    void preDestroy() {
        executor.shutdown();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> currentTask() {
        return CompletableFuture.supplyAsync(
                () -> getInfo(factory.getInstance()),
                executor
        );
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> asyncAssignTask() {
        return CompletableFuture.supplyAsync(() -> {
		ToDo todoManager = factory.getInstance();

		todoManager.setToDo(randomTask(), nextTaskID());

            return getInfo(todoManager);
        }, executor);
    }

    @POST
    @Path("sync")
    @Produces(MediaType.TEXT_PLAIN)
    public String assignTask() {
        ToDo todoManager = factory.getInstance();

	todoManager.setToDo(randomTask(), nextTaskID());

        return getInfo(todoManager);
    }

    private String getInfo(ToDo todoManager) {
        return Thread.currentThread().getName()
	    + ":  Next task=" + todoManager.getToDo(taskNumber);
    }

    private String randomTask ()
    {
	return tasks[rand.nextInt(tasks.length)];
    }
    
    synchronized private int nextTaskID ()
    {
	taskNumber++;

	if (taskNumber >= ToDo.MAX_TASKS)
	    taskNumber = 0;

	return taskNumber;
    }

    synchronized private int currentTaskID ()
    {
	return taskNumber;
    }    
    
    private static int taskNumber = 0;
    private static String[] tasks = {"todo - walk the dog", "todo - pay bills", "todo - write some Quarkus code!", "todo - play on PS4", "todo - read book", "todo - work!", "todo - read email"};
    private static Random rand = new Random();
}
