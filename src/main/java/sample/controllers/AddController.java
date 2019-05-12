package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import org.apache.log4j.Logger;
import sample.model.ObservableTaskList;
import sample.model.Task;
import sample.model.TaskException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * The class is used as a page event handler addScene.fxml
 * @author Ihor Kyrychenko
 */
public class AddController {

    private static Logger logger = Logger.getLogger(AddController.class);

    @FXML
    private TextField titleRep;

    @FXML
    private TextField titleUnRep;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private DatePicker timeDate;

    @FXML
    private AnchorPane first;

    @FXML
    private AnchorPane second;

    @FXML
    private AnchorPane third;

    private Spinner<LocalTime> startTime;

    private Spinner<LocalTime> endTime;

    private Spinner<LocalTime> timeTime;

    @FXML
    private TextField intervalRep;

    @FXML
    private CheckBox activeRep;

    @FXML
    private CheckBox activeUnRep;

    @FXML
    private Tab rep;

    private ObservableTaskList observableTaskList;

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

        startTime.setMinHeight(31);
        startTime.setMinWidth(120);
        endTime.setMinHeight(31);
        endTime.setMinWidth(120);
        timeTime.setMinHeight(31);
        timeTime.setMinWidth(120);

        first.getChildren().add(startTime);
        second.getChildren().add(endTime);
        third.getChildren().add(timeTime);

        startTime.setEditable(true);
        endTime.setEditable(true);
        timeTime.setEditable(true);
    }

    /**
     * Method for passing the task list to this class
     * @param observableTaskList - an object of class Task that will be changed
     */
    public void setTask(ObservableTaskList observableTaskList) {
        this.observableTaskList = observableTaskList;
    }

    /**
     * The method for adding a new task to the list
     * @param actionEvent - event what happened
     * @exception ParseException if the date field is incorrect
     * @exception TaskException if the title field is incorrect
     */
    public void add(ActionEvent actionEvent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (rep.isSelected()) {
                String titleText = titleRep.getText();
                if ("".equals(titleText))
                    throw new TaskException("The title field is empty.");

                if (titleText.length() > 100)
                    throw new TaskException("The size of the \"title\" field is greater than 100 characters.");

                if ("".equals(intervalRep.getText()))
                    throw new TaskException("The interval field is empty.");

                if (Integer.parseInt(intervalRep.getText()) < 0)
                    throw new TaskException("The interval field must be greater than 0");

                if (startDate.getValue() == null)
                    throw new TaskException("The start/date field is empty");

                if (startTime.getValue() == null)
                    throw new TaskException("The start/time field is empty");

                if (endDate.getValue() == null)
                    throw new TaskException("The end/date field is empty");

                if (endTime.getValue() == null)
                    throw new TaskException("The end/time field is empty");

                Date starts = dateFormat.parse(startDate.getValue().toString() + " " +
                                                    (startTime.getValue().toString().length() == 5 ?
                                                            startTime.getValue() + ":00" : startTime.getValue()
                                                    )
                );
                Date ends = dateFormat.parse(endDate.getValue().toString() + " " +
                                                (endTime.getValue().toString().length() == 5 ?
                                                        endTime.getValue() + ":00" : endTime.getValue()
                                                )
                );

                int intervals = Integer.parseInt(intervalRep.getText());
                boolean actives = activeRep.isSelected();

                observableTaskList.add(new Task(titleText, starts, ends, intervals, actives));

                logger.info("Repeatable task add in collection.");

                Stage s = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                s.close();
            } else {
                String titleText = titleUnRep.getText();
                if ("".equals(titleText))
                    throw new TaskException("The title field is empty.");

                if (titleText.length() > 100)
                    throw new TaskException("The size of the \"title\" field is greater than 100 characters.");

                if (timeDate.getValue() == null)
                    throw new TaskException("The time/date field is empty.");

                if (timeTime.getValue() == null)
                    throw new TaskException("The time/time field is empty.");

                Date times = timeDate.getValue().toString().isEmpty() ? null : dateFormat.parse(timeDate.getValue().toString() + " " + (timeTime.getValue().toString().length() == 5 ?
                                                                                                                                                timeTime.getValue() + ":00" : timeTime.getValue()
                                                                                                                                              )
                                                                                                );
                boolean actives = activeUnRep.isSelected();

                observableTaskList.add(new Task(titleText, times, actives));

                logger.info("Unrepeatable task add in collection.");

                Stage s = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                s.close();
            }
        } catch (TaskException e) {
            logger.error("In the window \"add\" the \"title\" field is empty.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.setWidth(500);
            alert.setHeight(200);
            alert.show();
        } catch (ParseException e) {
            logger.error("Wrong time entry in the \"add\" window.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Wrong time entry.");
            alert.show();
        } catch (DateTimeParseException e) {
            logger.error("Wrong time in the \"add\" window.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Wrong time entry.");
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
