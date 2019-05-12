package sample.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class of tasks with properties <b>title</b>, <b>time</b>, <b>start</b>, <b>end</b>, <b>interval</b>, <b>active</b>.
 * @author Ihor Kyrychenko
 */
public class Task implements Serializable {
    /** Task title field */
    private String title;
    /** Task time */
    private Date time = null;
    /** Task start time */
    private Date start = null;
    /** Task end time */
    private Date end = null;
    /** Task interval */
    private int interval;
    /** Task active */
    private boolean active;

    /** Constructor - creating a new object */
    public Task() {
    }

    /** Constructor - creating a new object with specific values
     * @param title - title
     * @param active - active
     */
    public Task(String title, boolean active) {
        this.title = title;
        this.active = active;
    }

    /** Constructor - creating a new object with specific values
     * @param title - title
     * @param time - task time
     * @param active - active
     */
    public Task(String title, Date time, boolean active) {
        this.title = title;
        this.time = time;
        this.active = active;
    }

    /** Constructor - creating a new object with specific values
     * @param title - title
     * @param start - task start time
     * @param end - task end time
     * @param interval - task interval
     * @param active - active
     */
    public Task(String title, Date start, Date end, int interval, boolean active) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.active = active;
        this.time = null;
    }


    /**
     * Constructor - creating a new object with specific values
     * @param title - title
     */
    public Task(String title) throws TaskException {
        setTitle(title);
    }

    /**
     * Constructor - creating a new object with specific values
     * @param title - title
     * @param time - task time
     */
    public Task(String title, Date time) throws TaskException {
        setTitle(title);
        this.time = time;
    }

    /**
     * Constructor - creating a new object with specific values
     * @param title - title
     * @param start - task start time
     * @param end - task end time
     * @param interval - task time
     */
    public Task(String title, Date start, Date end, int interval) throws TaskException {
        setTitle(title);
        setTime(start, end, interval);
    }

    /**
     * Method to determine the field {@link Task#time}
     * @param time - task time
     */
    public void setTime(Date time){
        this.time = time;
    }

    /**
     * Method to change non-repeatable task to repeatable
     * @param start - task start time
     * @param end - task end time
     * @param interval - task interval
     * @exception TaskException - time set incorrectly
     */
    public void setTime(Date start, Date end, int interval) throws TaskException {
        if (start.getTime() < 0 || end.getTime() < 0)
            throw new TaskException(" start time or end time < 0");
        if (interval <= 0)
            throw new TaskException(" interval time <= 0");
        time = null;
        this.start = start;
        this.end = end;
        this.interval = interval;
    }

    /**
     * Method to definition of next time task will need to show
     * @param current - the time relative to which the search takes place
     */
    public Date nextTimeAfter(Date current) {
        Date date = new Date();
        int a;
        a = interval * 1000;
        if (isRepeated() && active) {
            if (current.before(start) && !current.equals(start))
                return start;
            for (Date i = (Date) start.clone(); i.before(end); i.setTime(i.getTime() + a)) {
                if (current.getTime() >= i.getTime() && current.getTime() < i.getTime() + a && i.getTime() + a <= end.getTime()) {
                    date.setTime(i.getTime() + a);
                    return date;
                }
            }
        } else if (!isRepeated() && active) {
            if (time.after(current) && active)
                return time;
            else
                return null;
        }
        return null;
    }

    /**
     * Method to determine if the task is repeatable
     * @return returns a boolean value, true if the task is repeatable
     */
    public boolean isRepeated() {
        if (interval > 0)
            return true;
        else
            return false;
    }

    /**
     * Method to get the field {@link Task#time}
     * @return return task time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Method to determine the field {@link Task#title}
     * @param title - title
     * @exception TaskException - wrong title
     */
    public void setTitle(String title) throws TaskException {
        if (title == null)
            throw new TaskException(" title is empty");
        this.title = title;
    }

    /**
     * Method to get the field {@link Task#start}
     * @return returns task start time
     */
    public Date getStart() {
        return start;
    }

    /**
     * Method to determine the field {@link Task#start}
     * @param start - task start time
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Method to get the field {@link Task#end}
     * @return returns task end time
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Method to determine the field {@link Task#end}
     * @param end - task end time
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Method to get the field {@link Task#interval}
     * @return returns task interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Method to determine the field {@link Task#interval}
     * @param interval - task interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Method to get the field  {@link Task#title}
     * @return returns title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method to get the field {@link Task#active}
     * @return returns active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method to determine the field {@link Task#active}
     * @param active task active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Method to get the field {@link Task#start}
     * @return returns task start time
     */
    public Date getStartTime() {
        return start;
    }

    /**
     * Method to get the field  {@link Task#end}
     * @return returns task end time
     */
    public Date getEndTime() {
        return end;
    }

    /**
     * Method to get the field {@link Task#interval}
     * @return returns task interval
     */
    public int getRepeatInterval() {
        return interval;
    }

    /**
     * Method to output Task as a string
     * @return returns a string consisting of the elements of the class Task
     */
    public String toString() {
        return title + " " + time + " " + start + " " + end + " " + interval + " " + active + "\n";
    }

    /**
     * Method for comparing objects of class Task
     * @param object - the object with which this one is compared
     * @return returns a boolean value if two objects are equal
     */
    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;

        Task task = (Task) object;
        if (isRepeated())
            return title.equals(task.getTitle())
                    && start.equals(task.getStartTime())
                    && end.equals(task.getEndTime())
                    && interval == task.getRepeatInterval()
                    && active == task.isActive();
        else
            return title.equals(task.getTitle())
                    && time.equals(task.getTime())
                    && active == task.isActive();
    }

    /**
     * Method returning hashCode of class object
     * @return returns object hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        if (isRepeated()) {
            result = prime * result + ((title == null) ? 0 : title.hashCode());
            result = prime * result + start.hashCode();
            result = prime * result + end.hashCode();
            result = prime * result + interval;
            result = prime * result + (!active ? 0 : 1);
        } else {
            result = prime * result + ((title == null) ? 0 : title.hashCode());
            result = prime * result + time.hashCode();
            result = prime * result + (!active ? 0 : 1);
        }
        return result;
    }

    /**
     * Method to create a clone of the current object of class Task
     * @return returns a clone of the current object
     */
    @Override
    public Task clone() {
        Task task = new Task();
        try {
            task.setTitle(title);
            if (isRepeated()) {
                task.setTime(start, end, interval);
                task.time = this.time;
            } else {
                task.setTime(time);
                task.start = task.end = this.time;
                task.interval = this.interval;
            }
            task.setActive(active);
        } catch (TaskException e) {
            e.printStackTrace();
        }
        return task;
    }
}