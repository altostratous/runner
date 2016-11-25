package service;

import util.LongTask;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ali on 11/25/2016.
 * Java RMI is used.
 */
public class RunnerClient extends Observable{
    RunnerServerInterface server;
    Timer timer;
    public RunnerClient() throws RemoteException, NotBoundException, MalformedURLException {
        server = (RunnerServerInterface) Naming.lookup("//localhost/RunnerServer");
        timer = new Timer();
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

    public void addTask(File jobClassFile) throws Exception {
        server.putTask(jobClassFile);
    }
}
