package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import service.RunnerClient;
import ui.views.LongTaskView;
import util.LongTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the main GUI class. This is an application in which you can add tasks
 * and then manage them to run. You can, start, pause, cancel, restart and remove
 * tasks. Also the ui is beautiful thanks to javafx css compatibility.
 */
public class RunnerGUI extends Application implements Observer {

    /**
     * The LongTaskView container
     */
    @FXML
    private VBox tasksVBox;

    /**
     * Client to connect to RunnerServer
     */
    private RunnerClient client;

    private Window window;

    /**
     * The main method that launches the app.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The method wich initializes components
     * @param primaryStage
     * @throws IOException
     * @throws NotBoundException
     */
    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        // store window
        window = primaryStage;

        // javafx routines
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RunnerGUI.fxml"));
        loader.setController(this);
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Runner");

        // set close action
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        // connect to server
        client = new RunnerClient();

        // add this as its view
        client.addObserver(this);
    }


    /**
     * Handles +Add Task button in the GUI
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    @FXML
    public void addTask() throws MalformedURLException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Class. (*.class)", "*.class"));
        File jobClass = fileChooser.showOpenDialog(window);
        if (jobClass != null)
            try {
                client.putTask(jobClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Views downloaded from RunnerServer
     */
    HashMap<Integer, LongTaskView> views = new HashMap<>();

    /**
     * Updates task views based on the argument. The argument should be
     * A HashMap(Integer, LongTaskView)
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        HashMap<Integer, LongTask> tasks = (HashMap<Integer, LongTask>) arg;
        for (Integer key :
                tasks.keySet()) {

            // it there's a new task
            if (!views.containsKey(key)) {
                // if it is not removed
                if (tasks.get(key)!= null) {
                    // add new view
                    views.put(key, new LongTaskView(tasks.get(key), client, key));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tasksVBox.getChildren().add(views.get(key));
                        }
                    });
                }
            }

            // if the task is removed
            if (tasks.get(key) == null){
                // try to remove it
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (views.get(key)!= null) {
                            views.get(key).dispose();
                            tasksVBox.getChildren().remove(views.get(key));
                        }
                    }
                });
            }
            // if it is a common task try to update its view.
            if (views.get(key)!= null)
                views.get(key).update(tasks.get(key), null);
        }
    }
}
