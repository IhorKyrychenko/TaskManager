package sample.model;

import java.util.Iterator;

/**
 * Class for saving tasks in the array
 * @author Ihor Kyrychenko
 */
public class LinkedTaskList extends TaskList {
    /** The number of elements in the array */
    private int index = 0;

    /** An object of class Element storing the link to the beginning of the list */
    private Element start = null;

    /** An Element class object storing the link to the end of list link */
    private Element end = null;

    /** Inner class */
    private class Element {
        /** Task type object */
        Task task;

        /** An Element class object storing a link to the next element. */
        Element next = null;

        /** An Element class object storing a link to the previous element. */
        Element previous = null;

        /**
         * Element class constructor with parameters
         * @param value - Task type object
         */
        Element(Task value) {
            this.task = value;
        }

    }

    /** No-argument constructor */
    public LinkedTaskList() {
    }


    /**
     * Method to add tasks
     * @param task1 task to add
     */
    @Override
    public void add(Task task1) {
        if (task1 == null)
            System.out.println("Nothing added! Task is empty.");
        else {
            if (index == 0) {
                start = new Element(task1);
                end = start;
                index++;
            } else {
                end.next = new Element(task1);
                end.next.previous = end;
                end = end.next;
                index++;
            }
        }
    }

    /**
     * Method to add tasks
     * @param observableTaskList the list of tasks to be added to the list
     */
    @Override
    public void add(ObservableTaskList observableTaskList) {
        for (int i = 0; i < observableTaskList.size(); i++) {
            add(observableTaskList.getTask(i));
        }
    }

    /**
     * Method to delete a task from an list
     * @param task task to be deleted
     * @return returns a boolean value if the task existed
     */
    @Override
    public boolean remove(Task task) {
        if (task == null)
            return false;
        else {
            Element counter = start;

            while (counter != null) {
                if (!counter.task.equals(task)) {
                    counter = counter.next;
                } else {
                    if (counter.next == null) {
                        counter.task = null;
                        end = counter.previous;
                        counter = counter.previous;
                        counter.next = null;
                    } else if (counter.next.next == null) {
                        counter.task = counter.next.task;
                        counter.next = null;
                        end = counter;
                    } else {
                        counter.task = counter.next.task;
                        counter.next = counter.next.next;
                        counter = counter.next;
                        counter.previous = counter.previous.previous;
                    }
                    index--;
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Method that returns the number of elements in the linkedList
     * @return returns the number of elements in an linkedList
     */
    @Override
    public int size() {
        return index;
    }

    /**
     * Method to get the task by index
     * @param index1 Task number
     * @return return task with number index1
     */
    @Override
    public Task getTask(int index1) {
        int i;

        if (index1 <= index / 2) {
            i = 0;
            Element counter = start;

            while (counter != null) {
                if (i == index1) {
                    return counter.task;
                } else {
                    counter = counter.next;
                    i++;
                }
            }
        } else {
            i = index - 1;
            Element counter = end;

            while (counter != null) {
                if (i == index1) {
                    return counter.task;
                } else {
                    counter = counter.previous;
                    i--;
                }
            }
        }

        return null;
    }

    @Override
    public Iterator<Task> iterator() {
        Iterator<Task> it = new Iterator<Task>() {
            private Element element = start;
            private boolean vizov;

            @Override
            public boolean hasNext() {
                if (index == 0)
                    return false;
                return element != null;
            }

            @Override
            public Task next() {
                Task task;
                if (hasNext()) {
                    task = element.task;
                    element = element.next;
                } else {
                    return null;
                }
                vizov = true;
                return task;
            }

            @Override
            public void remove() throws IllegalStateException {
                if (hasNext() && vizov) {
                    LinkedTaskList.this.remove(element.previous.task);
                    vizov = false;
                } else {
                    throw new IllegalStateException();
                }
            }
        };
        return it;
    }

    /**
     * Method to return the task list as a string
     * @return returns a string consisting of objects of type Task
     */
    @Override
    public String toString() {
        Task task;
        String s = "LinkedTaskList{" +
                "n=" + index +
                "}: ";
        for (int i = 0; i < index; i++) {
            task = getTask(i);
            if (task.isRepeated())
                s += "\n[task №" + i + ", title = " + task.getTitle() +
                        ", startTime = " + task.getStartTime() + ", endTime = " +
                        task.getEndTime() + ", interval = " + task.getRepeatInterval() + ", active = " +
                        task.isActive() + ".]";
            else
                s += "\n[task №" + i + ", title = " + task.getTitle() +
                        ", time = " + task.getTime() + ", active = " +
                        task.isActive() + ".]";
        }
        return s;
    }

    /**
     * A method that returns a clone of the task list that calls the method.
     * @return returns a clone of this task list
     */
    @Override
    public LinkedTaskList clone() {
        LinkedTaskList ls = new LinkedTaskList();
        Element current = start;
        for (int i = 0; i < index; i++) {
            ls.add(current.task);
            current = current.next;
        }
        return ls;
    }
}
