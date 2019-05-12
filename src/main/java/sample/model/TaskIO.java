package sample.model;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for working with files
 * @author Ihor Kyrychenko
 */
public class TaskIO {

    private static Logger logger = Logger.getLogger(TaskIO.class);

    /**
     * Method for binary writing task list to file
     * @param tasks - list of tasks to be written to the file
     * @param out - write stream
     */
    public static void write(TaskList tasks, OutputStream out) throws IOException {
        try(out) {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(tasks.size());
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.getTask(i);
                os.writeObject(task.getTitle().length());
                os.writeObject(task.getTitle());
                os.writeObject(task.isActive() ? 1 : 0);
                os.writeObject(task.getRepeatInterval());
                if (task.isRepeated()) {
                    os.writeObject(task.getStartTime());
                    os.writeObject(task.getEndTime());
                } else {
                    os.writeObject(task.getTime());
                }
            }
        } finally {
            out.close();
        }

    }

    /**
     * Method for binary writing task list to file
     * @param tasks - list of tasks to be written to the file
     * @param file - File class object
     */
    public static void writeBinary(TaskList tasks, File file) throws IOException {
        try {
            write(tasks, new FileOutputStream(file.getPath()));
        } catch (FileNotFoundException e) {
            logger.error("File wasn't found during 'writeBinary' method call");
        }
    }

    /**
     * Method for binary reading the task list from a file
     * @param tasks - the list of tasks to which you want to write the tasks read from the file
     * @param in - stream to read
     */
    public static void read(TaskList tasks, InputStream in) throws IOException, ClassNotFoundException, TaskException  {
        try {
            ObjectInputStream o = new ObjectInputStream(in);
            int a = (int) o.readObject();
            for (int i = 0; i < a; i++) {
                int n = (int) o.readObject();
                String s = (String) o.readObject();
                int b = (int) o.readObject();
                int j = (int) o.readObject();
                Task task;
                if (j == 0) {
                    Date date = (Date) o.readObject();
                    task = new Task(s, date);
                } else {
                    Date date1 = (Date) o.readObject();
                    Date date2 = (Date) o.readObject();
                    task = new Task(s, date1, date2, j);
                }
                task.setActive(b == 1);
                tasks.add(task);
            }
        } finally {
            in.close();
        }

    }

    /**
     * Method for binary reading the task list from a file
     * @param tasks - the list of tasks to which you want to write the tasks read from the file
     * @param file - File class object
     */
    public static void readBinary(TaskList tasks, File file)  throws IOException, ClassNotFoundException, TaskException {
        try {
            read(tasks, new FileInputStream(file.getPath()));
        } catch (FileNotFoundException e) {
            logger.error("File wasn't found during 'readBinary' method call");
        }
    }


    /**
     * Method for text writing the task list to a file
     * @param tasks - list of tasks to be written to the file
     * @param out - write stream
     */
    public static void write(TaskList tasks, Writer out) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        PrintWriter printWriter = new PrintWriter(out);
        printWriter.print(tasks.size());
        out.write("\n");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);

            String s = task.getTitle();

            int a = s.indexOf("\"");
            if (a != -1) {
                String s1 = s.substring(0, a + 1) + "\"";
                String s2 = s.substring(a + 1);
                a = s2.indexOf("\"");
                String s3 = s2.substring(a + 1);
                s2 = s2.substring(0, a + 1) + "\"";
                s = "\"" + s1 + s2 + s3 + "\"";
            } else
                s = "\"" + s + "\"";

            if (task.isRepeated()) {
                s += " from [";
                Date date = task.getStartTime();
                s += dateFormat.format(date) + "] to [";
                date = task.getEndTime();
                s += dateFormat.format(date) + "] every [";

                int interval = task.getRepeatInterval();
                int day = interval / 86400;
                int hours = interval % 86400 / 3600;
                int minutes = interval % 3600 / 60;
                int sec = interval % 60;

                String a2 = "";
                a2 += (day != 0 ?  (day == 1 ? day + " day " : day + " days ") : "") +
                        (hours != 0 ?  (hours == 1 ? hours + " hour " : hours + " hours ") : "") +
                        (minutes != 0 ?  (minutes == 1 ? minutes + " minute " : minutes + " minutes ") : "") +
                        (sec != 0 ?  (sec == 1 ? sec + " second" : sec + " seconds") : "");
                if ("".equals(a2))
                    a2 += "0 second";
                s += a2 + "]";

            } else {
                s += " at [";
                Date date = task.getTime();
                s += dateFormat.format(date) + "]";
            }
            if (!task.isActive())
                s += " inactive";

            if (i != tasks.size() - 1)
                s += ";";
            else
                s += ".";

            out.write(s);
            out.write('\n');
        }
        out.close();
    }

    /**
     * Method for text reading the task list from a file
     * @param tasks - task list to which you want to write the read tasks from the file
     * @param in - stream to read
     */
    public static void read(TaskList tasks, Reader in) throws IOException, TaskException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BufferedReader reader = new BufferedReader(in);
        int size = Integer.parseInt(reader.readLine());
        String s;
        int j = 0;
        while (j < size) {
            s = reader.readLine();
            Task task = new Task();

            String s4;
            int a = s.indexOf("\"\"");
            if (a != -1) {
                String s1 = s.substring(1, a);
                int a1 = s.indexOf("\"\"", a + 1);
                String s2 = s.substring(a + 2, a1);
                a = s.indexOf("\"", a1 + 2);
                String s3 = s.substring(a1 + 2, a);
                s4 = s1 + s2 + s3;
            } else {
                a = s.indexOf("\"", 1);
                s4 = s.substring(1, a);
            }

            a = s.indexOf("from", a);
            String time;
            task.setTitle(s4);
            if (a == -1) {
                a = s.indexOf("[");
                int a1 = s.indexOf("]");
                time = s.substring(a + 1, a1);
                Date date = new Date();
                try {
                    date = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                task.setTime(date);
            } else {
                //start
                a = s.indexOf("[");
                int a1 = s.indexOf("]");
                time = s.substring(a + 1, a1);

                Date date1 = new Date();
                try {
                    date1 = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //end
                a = s.indexOf("[", a1);
                a1 = s.indexOf("]", a);
                time = s.substring(a + 1, a1);

                Date date2 = new Date();
                try {
                    date2 = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //interval
                int count = 0;
                a = s.indexOf("[", a1);
                a1 = s.indexOf("]", a);
                time = s.substring(a + 1, a1);

                String[] inte = time.split(" ");
                for (int i = 0; i < inte.length; i++) {
                    if (inte[i].equals("day") || inte[i].equals("days"))
                        count += 86400 * Integer.parseInt(inte[i - 1]);
                    if (inte[i].equals("hour") || inte[i].equals("hours"))
                        count += 3600 * Integer.parseInt(inte[i - 1]);
                    if (inte[i].equals("minute") || inte[i].equals("minutes"))
                        count += 60 * Integer.parseInt(inte[i - 1]);
                    if (inte[i].equals("second") || inte[i].equals("seconds"))
                        count += Integer.parseInt(inte[i - 1]);
                }
                task.setTime(date1, date2, count);
            }
            boolean b = false;
            if (s.indexOf("inactive") == -1)
                b = true;

            task.setActive(b);
            tasks.add(task);
            j++;
        }
        in.close();
    }

    /**
     * Method for text writing the task list to a file
     * @param tasks - list of tasks to be written to the file
     * @param file - File class object
     */
    public static void writeText(TaskList tasks, File file) throws IOException {
        FileWriter writer = new FileWriter(file.getPath());
        write(tasks, writer);
    }

    /**
     * Method for text reading the task list from a file
     * @param tasks - task list to which you want to write the read tasks from the file
     * @param file - File class object
     */
    public static void readText(TaskList tasks, File file) {
        try {
            FileReader reader = new FileReader(file.getPath());
            read(tasks, reader);
        } catch (FileNotFoundException e) {
            logger.error("File wasn't found during 'readText' method call");
        } catch (IOException e) {
            logger.error("IOException in 'readText' method");
        } catch (TaskException e) {
            logger.error("Error while writing a title (null) in 'readText' method");
        }

    }
}
