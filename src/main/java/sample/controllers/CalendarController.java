package sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sample.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class is used as a page event handler calendarScene.fxml
 * @author Ihor Kyrychenko
 */
public class CalendarController {

    private static Logger logger = Logger.getLogger(CalendarController.class);

    @FXML
    private DatePicker startTime;

    @FXML
    private DatePicker endTime;

    @FXML
    private TableColumn<ObservableMap.Entry<Date, Set<Task>>, String> dateColumn;

    @FXML
    private TableColumn<ObservableMap.Entry<Date, Set<Task>>, String> titleColumn;

    @FXML
    private TableView<ObservableMap.Entry<Date, Set<Task>>> tableCalendar;

    private ObservableTaskList observableTaskList;

    /**
     * Method for passing the task list to this class
     * @param observableTaskList - an object of class Task that will be changed
     */
    public void setTask(ObservableTaskList observableTaskList) {
        this.observableTaskList = observableTaskList;
    }

    /**
     * Method to close this window and unlock the main window
     * @param actionEvent - event what happened
     */
    public void back(ActionEvent actionEvent) {
        Stage s = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        s.close();
    }

    /**
     * Method to search among tasks by criteria
     * @param actionEvent - event what happened
     * @exception ParseException if the date field is incorrect
     * @exception TaskException if the title field is incorrect
     */
    public void search(ActionEvent actionEvent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {

            if (startTime.getValue() == null)
                throw new TaskException("The start time is empty.");

            if (endTime.getValue() == null)
                throw new TaskException("The end time is empty.");

            Date date1 = dateFormat.parse(startTime.getValue().toString());
            Date date2 = dateFormat.parse(endTime.getValue().toString());
            TaskList taskList = new LinkedTaskList();
            taskList.add(observableTaskList);
            TreeMap<Date, Set<Task>> setSortedMap = (TreeMap<Date, Set<Task>>) Tasks.calendar(taskList, date1, date2);

            if (setSortedMap.size() == 0)
                throw new TaskException("Nothing was found for the given parameters.");

            titleColumn.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue().getValue().toString()));

            dateColumn.setCellValueFactory((p) -> {
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new SimpleStringProperty(dateFormat1.format(p.getValue().getKey()));
            });

            tableCalendar.setItems(FXCollections.observableArrayList(setSortedMap.entrySet()));
        } catch (ParseException e) {
            logger.error("The date entered incorrectly in the calendar window");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Date entered incorrectly");
            alert.show();
        } catch (TaskException e) {
            logger.info("By the given criteria nothing has been found.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
