package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Ali on 11/25/2016.
 * This class is the main class in which the runner service is
 * implemented. Java RMI is used.
 */
public class RunnerServer extends UnicastRemoteObject implements RunnerServerInterface{
    /**
     * Creates a runner server on a specific port
     * @param port
     * @throws RemoteException
     */
    protected RunnerServer(int port) throws RemoteException {
        super(port);
    }
}
