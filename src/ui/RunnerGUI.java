package ui;/**
 * Created by HP PC on 11/25/2016.
 */

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

public class RunnerGUI extends Application implements Observer {

    @FXML
    private VBox tasksVBox;

    private RunnerClient client;

    private Window window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        window = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RunnerGUI.fxml"));
        loader.setController(this);
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Runner");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        client = new RunnerClient();
        client.addObserver(this);
    }

    @FXML
    public void addTask() throws MalformedURLException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        File jobClass = fileChooser.showOpenDialog(window);
        if (jobClass != null)
            try {
                client.putTask(jobClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    HashMap<Integer, LongTaskView> views = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        HashMap<Integer, LongTask> tasks = (HashMap<Integer, LongTask>) arg;
        for (Integer key :
                tasks.keySet()) {
            if (!views.containsKey(key)) {
                views.put(key, new LongTaskView(tasks.get(key), client, key));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tasksVBox.getChildren().add(views.get(key));
                    }
                });
            }
            views.get(key).update(tasks.get(key), null);
        }
    }
}
