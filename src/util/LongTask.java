package util;

import javafx.beans.Observable;

/**
 * Interface for all tasks that could be manage by the runner app.
 * To run and manage your task, you have to implement this interface
 * and add the *.class file to the runner application.
 */
public interface LongTask extends Runnable, Observable {
    void cancel();
    void start();
    void setRunner(RunnerInterface runner);
}
