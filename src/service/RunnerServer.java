package service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

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



    public void addTask(File jobClass) throws Exception {
        File url = jobClass.getParentFile();
        addTask(url, jobClass.getName().replaceAll("\\.class", ""));
    }
    public void addTask(File jobClass, String name) throws Exception {
        if (jobClass.getParentFile() == null)
            throw new Exception("Bad class.");
        URL url = jobClass.getParentFile().toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        try {
            urlClassLoader.loadClass(name);
        }catch (Exception ex){
            addTask(jobClass.getParentFile(), jobClass.getName() + "." + name);
        }
    }
}
