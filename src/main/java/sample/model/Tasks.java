package sample.model;

import java.util.*;

/**
 * Class to search for criteria
 * @author Ihor Kyrychenko
 */
public class Tasks {

    /**
     * Method to search for active tasks in a given interval
     * @param tasks - the list in which the search will take place
     * @param start - time to start searching
     * @param end - time to end searching
     * @return It returns a list, which is obtained by the search
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
        Iterable<Task> iterable = new HashSet<>();
        Iterator iterator = tasks.iterator();
        Task task;
        while (iterator.hasNext()) {
            task = (Task) iterator.next();
            if (task.isActive()) {
                if (task.isRepeated()) {
                    if (task.getStartTime().getTime() > end.getTime() || task.getEndTime().getTime() < start.getTime()) {
                        continue;
                    }
                    Date date = task.nextTimeAfter(start);
                    if (date != null && date.getTime() <= end.getTime()) {
                        ((HashSet<Task>) iterable).add(task);
                    }
                } else {
                    if (task.getTime().getTime() <= end.getTime() && task.getTime().getTime() > start.getTime())
                        ((HashSet<Task>) iterable).add(task);
                }
            }
        }
        return iterable;
    }

    /**
     * Method returns an object of type SortedMap
     * @param tasks - the list in which the search will take place
     * @param start - time to start searching
     * @param end - time to end searching
     * @return returns an object of type SortedMap consisting of a key of type Date
     * and values of type Set consisting of objects of type Task
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        SortedMap<Date, Set<Task>> sortedMap = new TreeMap<>();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (!task.isActive())
                continue;
            if (!task.isRepeated()) {
                Date date = task.nextTimeAfter(start);
                if (date != null && date.getTime() <= end.getTime()) {
                    HashSet<Task> set = (HashSet<Task>) sortedMap.get(date);
                    if (set == null) {
                        set = new HashSet<>();
                        set.add(task);
                        sortedMap.put(date, set);
                    } else {
                        set.add(task);
                    }
                }
            } else {
                Date date2 = (Date) start.clone();
                while (date2.getTime() <= task.getEndTime().getTime()){
                    Date date1 = task.nextTimeAfter(date2);
                    if (date1 != null && date1.getTime() <= end.getTime()) {
                        HashSet<Task> set = (HashSet<Task>) sortedMap.get(date1);
                        if (set == null) {
                            set = new HashSet<>();
                            set.add(task);
                            sortedMap.put(date1, set);
                        } else {
                            set.add(task);
                        }
                        date2 = date1;
                    } else {
                        break;
                    }
                }
            }
        }
        return sortedMap;
    }
}
