package ui.views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import service.RunnerClient;
import util.LongTask;
import util.LongTaskStatus;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ali on 11/25/2016.
 * Provides view for a specific LongTask. This is used in the GUI.
 */
public class LongTaskView extends AnchorPane implements Observer{
    private ProgressBar progressBar;
    private Label label;
    private Label name;
    private Button startButton;
    private Button pauseButton;
    private Button cancelButton;
    private LongTask task;
    private RunnerClient client;
    private int id;


    public LongTaskView(LongTask task, RunnerClient client, int id){
        this.client = client;
        this.id = id;
        progressBar = new ProgressBar(0);
        setTask(task);
        label = new Label("Not started yet.");
        name = new Label(task.getDisplayName());
        startButton = new Button("Start");
        pauseButton = new Button("Pause");
        cancelButton = new Button("Cancel");
        pauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                          @Override
                                          public void handle(MouseEvent event) {
                                              try {
                                                  pauseButtonClicked();
                                              } catch (RemoteException e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      });
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                try {
                                                    startButtonClicked();
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            }
        });
        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                           @Override
                                           public void handle(MouseEvent event) {
                                               try {
                                                   cancelButtonClicked();
                                               } catch (RemoteException e) {
                                                   e.printStackTrace();
                                               }
                                           }
                                       });
        getChildren().add(progressBar);
        getChildren().add(name);
        getChildren().add(label);
        getChildren().add(startButton);
        setRightAnchor(startButton, 10.);
        setBottomAnchor(startButton, 10.);
        setBottomAnchor(label, 5.);
        setLeftAnchor(label, 10.);
        setBottomAnchor(name, 25.);
        setLeftAnchor(name, 10.);
        setTopAnchor(progressBar, 10.);
        setLeftAnchor(progressBar, 5.);
        setRightAnchor(progressBar, 10.);
        setMinHeight(70);
        setStyle(
                "-fx-background-color: rgba(240,230,250,1);"
        );
    }

    private void startButtonClicked() throws RemoteException {
        client.startTask(id);
    }

    private void cancelButtonClicked() throws RemoteException {
        client.cancellTask(id);
    }

    private void pauseButtonClicked() throws RemoteException {
        client.pauseTask(id);
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LongTaskView.this.setTask((LongTask) o);
                name.setText(task.getDisplayName());
                label.setText(task.getStatus().toString());
                progressBar.setProgress(task.getProgress());
                switch (task.getStatus()){
                    case Paused:
                        startButton.setText("Resume");
                        break;
                    case NotStartedYet:
                        startButton.setText("Start");
                        break;
                    case Crashed:
                    case Completed:
                    case Cancelled:
                        startButton.setText("Restart");
                }
                if (task.getStatus() == LongTaskStatus.Running){
                    pauseButton.setVisible(true);
                    startButton.setVisible(false);
                    cancelButton.setVisible(true);
                }
                else
                {
                    pauseButton.setVisible(false);
                    startButton.setVisible(true);
                    cancelButton.setVisible(false);
                }
            }
        });
    }

    public void setTask(LongTask task) {
        this.task = task;
        task.addObserver(this);
    }
}
