package service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Ali on 11/25/2016.
 * Java RMI is used.
 */
public class RunnerClient {
    RunnerServerInterface server;
    public RunnerClient() throws RemoteException, NotBoundException, MalformedURLException {
        server = (RunnerServerInterface) Naming.lookup("//localhost/RunnerServer");
    }
}
