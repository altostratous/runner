package service;

import java.io.File;
import java.rmi.Remote;

/**
 * Created by HP PC on 11/25/2016.
 */
public interface RunnerServerInterface extends Remote {
    void addTask(File jobClassFile) throws Exception;
}
