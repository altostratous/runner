package service;

import util.LongTask;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ali on 11/25/2016.
 * Java RMI is used to connect to the server.
 */
public class RunnerClient extends Observable implements RunnerServerInterface{
    /**
     * The server interface used to communicate
     */
    RunnerServerInterface server;

    /**
     * The timer used to refresh tasks states
     */
    Timer timer;
    public RunnerClient() throws RemoteException, NotBoundException, MalformedURLException {
        // RMI routine
        server = (RunnerServerInterface) Naming.lookup("//localhost/RunnerServer");
        timer = new Timer();

        // for each 500 milliseconds get tasks from server
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                        HashMap<Integer, LongTask> updatedTasks = server.getTasks();
                        setChanged();
                        notifyObservers(updatedTasks);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },0, 500);
    }

    /**
     * Registers a task to the server sending the address to the javaclass file
     * @param jobClassFile
     * @throws Exception
     */
    public void putTask(File jobClassFile) throws Exception {
        server.putTask(jobClassFile);
    }


    /**
     * Removes a task with a specific id
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void removeTask(Integer id) throws RemoteException {
        server.removeTask(id);
    }


    /**
     * Starts a task on the server
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void startTask(Integer id) throws RemoteException {
        server.startTask(id);
    }

    /**
     * Sends cancellation request to the server for a specific task
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void cancelTask(Integer id) throws RemoteException {
        server.cancelTask(id);
    }


    /**
     * Sends pause request to the server for a specific task
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void pauseTask(Integer id) throws RemoteException {
        server.pauseTask(id);
    }

    /**
     * Sends resume request to the server
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void resumeTask(Integer id) throws RemoteException {
        server.resumeTask(id);
    }

    /**
     * Obtains all tasks on the server
     * @return a HashMap(Integer, LongTask) in which keys are unique identifiers for tasks. Removed tasks are null but
     *         their id will be there
     * @throws RemoteException
     */
    @Override
    public HashMap<Integer, LongTask> getTasks() throws RemoteException {
        return server.getTasks();
    }
}
