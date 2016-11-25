package ui.views;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import util.LongTask;

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
    private Button button;
    private LongTask task;
    public LongTaskView(LongTask task){
        progressBar = new ProgressBar(0);
        setTask(task);
        label = new Label("Not started yet.");
        name = new Label(task.getDisplayName());
        button = new Button("Start");
        getChildren().add(progressBar);
        getChildren().add(name);
        getChildren().add(label);
        getChildren().add(button);
        setRightAnchor(button, 10.);
        setBottomAnchor(button, 10.);
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

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LongTaskView.this.setTask((LongTask) o);
                name.setText(task.getDisplayName());
                label.setText(task.getStatus().toString());
                progressBar.setProgress(task.getProgress());
            }
        });
    }

    public void setTask(LongTask task) {
        this.task = task;
        task.addObserver(this);
    }
}
