package service;

import util.LongTask;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ali on 11/25/2016.
 * This class is the main class in which the runner service is
 * implemented. Java RMI is used.
 */
public class RunnerServer extends UnicastRemoteObject implements RunnerServerInterface{
    /**
     * Creates a runner server on a specific port
     * @throws RemoteException maybe
     */
    public RunnerServer() throws RemoteException {
        super(0);
    }

    /**
     * Starts RunnerServer
     * @param args  args to the program, no meaning
     * @throws RemoteException if it fails to start the server on network
     * @throws MalformedURLException it doesn't happen
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        //Instantiate RmiServer

        RunnerServer runnerServer = new RunnerServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/RunnerServer", runnerServer);
        System.out.println("PeerServer bound in registry");
    }

    /**
     * Tasks on the server
     */
    ArrayList<LongTask> tasks = new ArrayList<>();


    /**
     * Registers a task to the server sending the address to the javaclass file
     * @param   jobClass the file path to the class file
     * @throws Exception if file is not valid
     */
    public void putTask(File jobClass) throws Exception {
        File url = jobClass.getParentFile();
        putTask(url, jobClass.getName().replaceAll("\\.class", ""));
    }


    /**
     * Removes a task with a specific id
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     */
    @Override
    public void removeTask(Integer id) {

        tasks.get(id).terminate();
        tasks.set(id, null);
    }


    /**
     * Starts a task on the server
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void startTask(Integer id) throws RemoteException {
        tasks.get(id).restart();
    }


    /**
     * Sends cancellation request to the server for a specific task
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException if doesn't supports cancellation
     */
    @Override
    public void cancelTask(Integer id) throws RemoteException {
        try {
            tasks.get(id).cancel();
        } catch (Exception e) {
            throw new RemoteException();
        }
    }

    /**
     * Sends pause request to the server for a specific task
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void pauseTask(Integer id) throws RemoteException {
        try {
            tasks.get(id).pause();
        } catch (Exception e) {
            throw  new RemoteException();
        }
    }

    /**
     * Sends resume request to the server
     * @param id unique identifier for the task. This is assigned when task is registered on the server and can be
     *           obtained calling getTasks method of the server
     * @throws RemoteException
     */
    @Override
    public void resumeTask(Integer id) throws RemoteException {
        tasks.get(id).restart();
    }

    /**
     * Obtains all tasks on the server
     * @return a HashMap(Integer, LongTask) in which keys are unique identifiers for tasks. Removed tasks are null but
     *         their id will be there
     * @throws RemoteException maybe
     */
    @Override
    public HashMap<Integer, LongTask> getTasks() throws RemoteException{
        HashMap<Integer, LongTask> updatedTasks = new HashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            {
                updatedTasks.put(i, tasks.get(i));
                if (tasks.get(i) != null) {
                    tasks.get(i).notifyObservers();
                }
            }
        }
        return updatedTasks;
    }

    /**
     * The inner recursive function to find the class path of a specified java class
     * @param jobClass file name of the class
     * @param name name of the class
     * @throws Exception If the file is not valid
     */
    public void putTask(File jobClass, String name) throws Exception {
        if (jobClass.getParentFile() == null)
            throw new Exception("Bad class.");
        URL url = jobClass.getParentFile().toURI().toURL();

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        try {
            Class<? extends LongTask> longTaskClass = (Class<? extends LongTask>) urlClassLoader.loadClass(name);
            LongTask task = longTaskClass.newInstance();
            tasks.add(task);
            // return tasks.get(tasks.size() - 1);
        }catch (Exception ex){
            putTask(jobClass.getParentFile(), jobClass.getName() + "." + name);
        }
        // return null;
    }
}
