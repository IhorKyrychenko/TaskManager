package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sample.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


/**
 * The class is used as a page event handler mainScene.fxml
 * @author Ihor Kyrychenko
 */
public class MainController {

    private static Logger logger = Logger.getLogger(MainController.class);

    private ObservableTaskList observableTaskList = new ObservableTaskList();

    @FXML
    private TableView<Task> table;

    @FXML
    private TableColumn<Task, String> columnTitle;

    @FXML
    private TableColumn<Task, String> columnTime;

    @FXML
    private TableColumn<Task, String> columnStart;

    @FXML
    private TableColumn<Task, String> columnEnd;

    @FXML
    private TableColumn<Task, String> columnInterval;

    @FXML
    private TableColumn<Task, String> columnActive;


    /** The method that is called when the window starts, sets the initial parameters in the main window */
    @FXML
    private void initialize() {

        TaskList taskList = new ArrayTaskList();
        File file = new File("tasks.txt");

        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.error("An error occurred while creating the file.");
        }

        if (!(file.length() == 0))
            TaskIO.readText(taskList, file);

        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        columnInterval.setCellValueFactory(new PropertyValueFactory<>("interval"));
        columnActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        observableTaskList.add(taskList);
        table.setItems(observableTaskList.getTasks());
        logger.info("There was no error when filling the table");

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (observableTaskList) {
                        Date date = new Date();

                        for (int i = 0; i < observableTaskList.size(); i++) {
                            Task task = observableTaskList.getTask(i);

                            if (task.nextTimeAfter(date) != null && Math.abs(date.getTime() - task.nextTimeAfter(date).getTime()) < 1000) {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText("The task - " + task.getTitle() + " must be completed.");
                                    alert.show();
                                });

                            }
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        myThread.start();
    }

    /**
     * The method of transition to the add task window
     * @param actionEvent - event what happened
     * @exception IOException window failed to load
     */
    public void add(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/addScene.fxml"));
            Parent root = fxmlLoader.load();
            logger.info("Add window loaded");
            ((AddController) fxmlLoader.getController()).setTask(observableTaskList);

            stage.setResizable(false);
            stage.setTitle("Add Task");
            stage.setMinWidth(420);
            stage.setMinHeight(420);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            logger.error("File not found");
            System.out.println(e.getMessage());
        }
    }

    /**
     * The method of transition to the calendar window, making the transfer observableTaskList
     * @param actionEvent - event what happened
     * @exception IOException window failed to load
     */
    public void calendar(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/calendarScene.fxml"));
            Parent root = fxmlLoader.load();
            logger.info("The calendar window loaded successfully");
            ((CalendarController) fxmlLoader.getController()).setTask(observableTaskList);
            stage.setTitle("Calendar");
            stage.setScene(new Scene(root));
            stage.setMinHeight(540);
            stage.setMinWidth(600);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            logger.error("File path was not found");
            System.out.println(e.getMessage());
        }
    }

    /**
     * The exit method from the main window, saving tasks in a file
     * @param actionEvent - event what happened
     * @exception IOException information could not be written to the file
     */
    public void exit(ActionEvent actionEvent) {
        try {
            TaskList taskList = new LinkedTaskList();
            taskList.add(observableTaskList);
            TaskIO.writeText(taskList, new File("tasks.txt"));
            logger.info("Entry at the closing of the file is successful");
        } catch (IOException e) {
            logger.error("The file to record was not found");
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Method to delete the selected task
     * @param actionEvent - event what happened
     */
    public void delete(ActionEvent actionEvent) {
        try {
            Task task = table.getSelectionModel().getSelectedItem();
            if (task == null)
                throw new TaskException("Choose the task you want.");
            observableTaskList.delete(task);
            logger.info("Task " + task.toString() + " successfully deleted");
        } catch (TaskException e) {
            logger.info("The user did not select the task to delete");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * Method to go to the task change window, making the transfer of the selected task
     * @param actionEvent - event what happened
     * @exception IOException if there was an error loading the page
     * @exception TaskException if the task is not selected
     */
    public void change(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/changeScene.fxml"));
            Parent root = loader.load();
            Task task = table.getSelectionModel().getSelectedItem();
            if (task == null)
                throw new TaskException("Choose the task you want.");
            logger.info("The loading of the change window is successful");
            ((ChangeController) loader.getController()).setTask(task);

            stage.setTitle("Change Task");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            logger.error("File changeScene.fxml not found");
            System.out.println(e.getMessage());
        } catch (TaskException e) {
            logger.info("The task was not selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * Method for updating table information
     * @param actionEvent - event what happened
     */
    public void refresh(ActionEvent actionEvent) {
        logger.info("Update task list");
        table.refresh();
    }
}
