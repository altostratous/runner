package util;

import java.util.Observer;

/**
 * This is interface to interact the the runner app from
 * within the user tasks
 */
public interface RunnerInterface extends Observer{
    void addTask(LongTask task);
    void removeTask(LongTask task);
    void startTask(LongTask task);
}
