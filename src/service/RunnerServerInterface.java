package service;

import util.LongTask;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by HP PC on 11/25/2016.
 */
public interface RunnerServerInterface extends Remote {
    void putTask(File jobClassFile) throws Exception;
    void removeTask(Integer id) throws RemoteException;
    HashMap<Integer,LongTask> getTasks() throws RemoteException;
}
