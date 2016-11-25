package service;

import util.LongTask;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by HP PC on 11/25/2016.
 * RMI server interface class.
 */
public interface RunnerServerInterface extends Remote {
    /**
     * Put task on server
     * @param jobClassFile task class file
     * @throws Exception
     */
    void putTask(File jobClassFile) throws Exception;

    /**
     * Remove task from server
     * @param id
     * @throws RemoteException
     */
    void removeTask(Integer id) throws RemoteException;

    /**
     * Start task
     * @param id
     * @throws RemoteException
     */
    void startTask(Integer id) throws RemoteException;

    /**
     * Cancel task
     * @param id
     * @throws RemoteException
     */
    void cancelTask(Integer id) throws RemoteException;

    /**
     * Pause task
     * @param id
     * @throws RemoteException
     */
    void pauseTask(Integer id) throws RemoteException;

    /**
     * Resume task
     * @param id
     * @throws RemoteException
     */
    void resumeTask(Integer id) throws RemoteException;

    /**
     * Obtain all tasks
     * @return
     * @throws RemoteException
     */
    HashMap<Integer,LongTask> getTasks() throws RemoteException;
}
