package org.acme.quickstart.stm;

import org.jboss.stm.annotations.Transactional;

/**
 * This is a pretty basic implementation of ToDo as
 * we want to focus on the principles of the STM
 * implementation.
 */

@Transactional
public interface ToDo {
    // Maximum number of tasks we can manage.
    
    public static final int MAX_TASKS = 100;

    /**
     * Tasks are arbitary Strings and identified using
     * unique ids.
     */
    
    String getToDo (int id);  // get task given its id
    void setToDo (String task, int id); // set task and id
}
