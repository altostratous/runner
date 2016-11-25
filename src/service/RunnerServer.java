package service;

import java.net.MalformedURLException;
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
}
