package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import org.apache.log4j.Logger;
import sample.model.Task;
import sample.model.TaskException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.FormatStyle;


/**
 * The class is used as a page event handler changeScene.fxml
 * @author Ihor Kyrychenko
 */
public class ChangeController extends Spinner<LocalTime> {

    private static Logger logger = Logger.getLogger(ChangeController.class);

    @FXML
    private AnchorPane start;

    @FXML
    private AnchorPane end;

    @FXML
    private AnchorPane time;

    @FXML
    private Tab firstTab;

    private Spinner<LocalTime> startTime;

    private Spinner<LocalTime> endTime;

    private Spinner<LocalTime> timeTime;

    @FXML
    private DatePicker timeDate;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField titleRep;

    @FXML
    private TextField interval;

    @FXML
    private TextField titleUnRep;

    @FXML
    private CheckBox activeRep;

    @FXML
    private CheckBox activeUnRep;

    private Task task;

    public void initialize() {
        startTime = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {

            {
                setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
            }

            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.minusHours(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.plusHours(steps));
                }
            }
        });
        endTime = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {

            {
                setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
            }

            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.minusHours(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.plusHours(steps));
                }
            }
        });
        timeTime = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {

            {
                setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
            }

            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.minusHours(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.plusHours(steps));
                }
            }
        });

        start.getChildren().add(startTime);
        end.getChildren().add(endTime);
        time.getChildren().add(timeTime);

        startTime.setMinHeight(31);
        startTime.setMinWidth(120);
        endTime.setMinHeight(31);
        endTime.setMinWidth(120);
        timeTime.setMinHeight(31);
        timeTime.setMinWidth(120);

        startTime.setEditable(true);
        endTime.setEditable(true);
        timeTime.setEditable(true);

    }

    /**
     * Method to fill fields with appropriate values
     * @param task - an object of class Task that will be changed
     */
    public void setTask(Task task) {
        this.task = task;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!task.isRepeated()) {
            titleUnRep.setText(task.getTitle());

            String s = dateFormat.format(task.getTime());
            int a = s.indexOf(" ");
            timeDate.setPromptText(s.substring(0, a));
            timeTime.getEditor().setPromptText(s.substring(a + 1, s.length()));

            activeUnRep.setSelected(task.isActive());
        } else {
            titleRep.setText(task.getTitle());

            String s = dateFormat.format(task.getStart());
            int a = s.indexOf(" ");
            startDate.setPromptText(s.substring(0, a));
            startTime.getEditor().setPromptText(s.substring(a + 1, s.length()));

            s = dateFormat.format(task.getEnd());
            endDate.setPromptText(s.substring(0, a));
            endTime.getEditor().setPromptText(s.substring(a + 1, s.length()));

            interval.setText(Integer.toString(task.getInterval()));
            activeRep.setSelected(task.isActive());
        }
    }

    /**
     * Method to change the task
     * @param actionEvent - event what happened
     * @exception TaskException if the title field is incorrect
     * @exception ParseException if the date field is incorrect
     */
    public void change(ActionEvent actionEvent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (firstTab.isSelected()) {
                if ("".equals(titleRep.getText()))
                    throw new TaskException("The title field is empty.");

                if (titleRep.getText().length() > 100)
                    throw new TaskException("The size of the \"title\" field is greater than 100 characters.");

                if ("".equals(interval.getText()))
                    throw new TaskException("The interval field is empty.");

                if (Integer.parseInt(interval.getText()) < 0)
                    throw new TaskException("The interval field must be greater than 0");

                if (startDate.getValue() == null)
                    throw new TaskException("The start/date field is empty");

                if (startTime.getValue() == null)
                    throw new TaskException("The start/time field is empty");

                if (endDate.getValue() == null)
                    throw new TaskException("The end/date field is empty");

                if (endTime.getValue() == null)
                    throw new TaskException("The end/time field is empty");

                task.setTitle(titleRep.getText());
                task.setTime(dateFormat.parse(startDate.getValue().toString() + " " + (startTime.getValue().toString().length() == 5 ? startTime.getValue() + ":00" : startTime.getValue())),
                             dateFormat.parse(endDate.getValue().toString() + " " + (endTime.getValue().toString().length() == 5 ? endTime.getValue() + ":00" : endTime.getValue())),
                             Integer.parseInt(interval.getText()));
                task.setActive(activeRep.isSelected());
            } else {
                if ("".equals(titleUnRep.getText()))
                    throw new TaskException("The title field is empty.");

                if (titleUnRep.getText().length() > 100)
                    throw new TaskException("The size of the \"title\" field is greater than 100 characters.");

                if (timeDate.getValue() == null)
                    throw new TaskException("The time/date field is empty.");

                if (timeTime.getValue() == null)
                    throw new TaskException("The time/time field is empty.");

                task.setTitle(titleUnRep.getText());
                task.setTime(dateFormat.parse(timeDate.getValue() + " " +
                                                    (timeTime.getValue().toString().length() == 5 ?
                                                            timeTime.getValue() + ":00" : timeTime.getValue()
                                                    )
                                             )
                );
                task.setStart(null);
                task.setEnd(null);
                task.setInterval(0);
                task.setActive(activeUnRep.isSelected());
            }

            Stage s = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            logger.info("Task change successful.");
            s.close();
        } catch (TaskException e) {
            logger.info("Incorrect data format in the \"title\" field in the \"change\" window.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.show();
        } catch (ParseException e) {
            logger.info("Incorrect data format in the \"date\" field in the \"change\" window.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Date entered incorrectly.");
            alert.show();
        }
    }

    /**
     * Method to close this window and unlock the main window
     * @param actionEvent - event what happened
     */
    public void back(ActionEvent actionEvent) {
        Stage s = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        s.close();
    }
}
