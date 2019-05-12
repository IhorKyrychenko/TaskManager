package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class for use in the TableView
 * @author Ihor Kyrychenko
 */
public class ObservableTaskList {

    /** Task list that reacts to changes, consisting of objects of type Task */
    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    /**
     * Method to get the field  {@link ObservableTaskList#tasks}
     * @return returns list
     */
    public ObservableList<Task> getTasks() {
        return tasks;
    }

    /**
     * Method to determine the field  {@link ObservableTaskList#tasks}
     * @param tasks - task list
     */
    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Method to add tasks
     * @param task - task to add to tasks
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Method to add list of tasks
     * @param taskList - task list to add to tasks
     */
    public void add(TaskList taskList) {
        if (taskList == null)
            return;
        for (Task task : taskList) {
            tasks.add(task);
        }
    }

    /**
     * Method to get the task by it's index
     * @param index - task number to get
     * @return returns task by index
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Method to get the size of tasks
     * @return returns size
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Method to delete a task
     * @param task - task that will be deleted
     */
    public void delete(Task task) {
        tasks.remove(task);
    }
}
