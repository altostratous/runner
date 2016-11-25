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
     * @throws RemoteException
     */
    public RunnerServer() throws RemoteException {
        super(0);
    }

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

    ArrayList<LongTask> tasks = new ArrayList<>();

    public void putTask(File jobClass) throws Exception {
        File url = jobClass.getParentFile();
        putTask(url, jobClass.getName().replaceAll("\\.class", ""));
    }
    @Override
    public void removeTask(Integer id) {

        tasks.get(id).terminate();
        tasks.set(id, null);
    }

    @Override
    public void startTask(Integer id) throws RemoteException {
        tasks.get(id).restart();
    }

    @Override
    public void cancellTask(Integer id) throws RemoteException {
        try {
            tasks.get(id).cancel();
        } catch (Exception e) {
            throw new RemoteException();
        }
    }

    @Override
    public void pauseTask(Integer id) throws RemoteException {
        try {
            tasks.get(id).pause();
        } catch (Exception e) {
            throw  new RemoteException();
        }
    }

    @Override
    public void resumeTask(Integer id) throws RemoteException {
        tasks.get(id).restart();
    }

    @Override
    public HashMap<Integer, LongTask> getTasks() {
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
