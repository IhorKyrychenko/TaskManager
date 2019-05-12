package sample.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 * Class for storing Task type objects
 * @author Ihor Kyrychenko
 */
abstract public class TaskList implements Iterable<Task>, Serializable {

    /**
     * Method to add tasks
     * @param task - task to add to list
     */
    abstract public void add(Task task);

    /**
     * Method to delete a task
     * @param task task to be deleted
     * @return returns a boolean value if the existence of a remote task
     */
    abstract public boolean remove(Task task);

    /**
     * Method that returns the number of elements in the list
     * @return returns the number of elements in an list
     */
    abstract public int size();

    /**
     * Method to get the task by index
     * @param index1 Task number
     * @return returns the task that number index1
     */
    abstract public Task getTask(int index1);

    /**
     * A method that returns a clone of the task list that calls the method.
     * @return returns a clone of this task list
     */
    abstract public TaskList clone();

    /**
     * Method to add tasks
     * @param observableTaskList the list of tasks to be added to the list
     */
    abstract public void add(ObservableTaskList observableTaskList);

    /**
     * Method for determining whether task lists are equal
     * @param  tasks the list with which the data will be compared
     * @return returns a boolean value, equality of lists
     */
    public boolean equals(TaskList tasks) {
        boolean b = false;
        int j = 0;
        for (int i = 0; i < size(); i++) {
            if (this.getTask(i).equals(tasks.getTask(i)))
                j++;
        }
        if (j == size())
            return true;
        return b;
    }

    /**
     * Method to return the hash code
     * @return returns list hash code value
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        Iterator<Task> iterator = this.iterator();
        while (iterator.hasNext()) {
            result += prime + iterator.next().hashCode();
        }
        return result;
    }

    /**
     * Method for finding active tasks in the gap
     * @param from the beginning of the gap
     * @param to   the end of gap
     * @return returns a list of tasks that will be executed at least once more
     */

    public TaskList incoming(Date from, Date to) {
        TaskList arrayTaskList = new ArrayTaskList();
        Date date = new Date();

        for (int i = 0; i < size(); i++) {
            if (getTask(i).nextTimeAfter(from).before(to)) {
                arrayTaskList.add(getTask(i));
            }
        }
        return arrayTaskList;
    }
}
