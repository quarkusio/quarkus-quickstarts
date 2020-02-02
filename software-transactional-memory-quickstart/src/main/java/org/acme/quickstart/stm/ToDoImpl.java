package org.acme.quickstart.stm;

import org.jboss.stm.annotations.ReadLock;
import org.jboss.stm.annotations.WriteLock;
import org.jboss.stm.annotations.LockFree;

/**
 * No error checking for simplicity ... it's a basic example!
 */

public class ToDoImpl implements ToDo
{
    public ToDoImpl ()
    {
	_tasks = new String[ToDo.MAX_TASKS];
    }

    @ReadLock
    public String getToDo (int id)
    {
	return _tasks[id];
    }

    @WriteLock
    public void setToDo (String task, int id)
    {
	_tasks[id] = task;
    }

    private String[] _tasks;
}
