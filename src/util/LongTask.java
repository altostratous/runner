package util;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.SplittableRandom;

/**
 * Interface for all tasks that could be manage by the runner app.
 * To run and manage your task, you have to implement this interface
 * and add the *.class file to the runner application.
 */
public abstract class LongTask extends Observable implements Runnable, Serializable{
    // flags
    protected boolean pleaseCancel;
    protected boolean pleasePause;
    private Object state;
    public void pause() throws Exception {
        if (!getSupportsPause())
            throw new Exception("Task doesn't support pause.");
        pleasePause = true;
    }
    protected boolean pleaseBreak(){
        return pleaseCancel || pleasePause;
    }
    protected Object getState() {
        return state;
    }

    protected void setState(Object state) {
        this.state = state;
    }

    protected double progress;
    public void cancel() throws Exception {
        if (!getSupportsCancellation()){
            throw new Exception("Task doen't support cancellation.");
        }
        pleaseCancel = true;
    }

    private boolean supportsPause = true;
    private boolean supportsCancellation = true;

    protected void setSupportsPause(boolean supportsPause){
        this.supportsPause = supportsPause;
    }

    protected void setSupportsCancellation(boolean supportsCancellation){
        this.supportsCancellation = supportsCancellation;
    }

    public boolean getSupportsPause() {
        return supportsPause;
    }

    public boolean getSupportsCancellation() {
        return supportsCancellation;
    }

    private String displayName;
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private LongTaskStatus status;

    public LongTaskStatus getStatus() {
        return status;
    }
    protected void setStatus(LongTaskStatus status){
        this.status = status;
    }

    public double getProgress() {
        return progress;
    }

    public LongTask(){
        setChanged();
        setStatus(LongTaskStatus.NotStartedYet);
    }

    public abstract void terminate();

    private transient Thread thread;

    public void restart() throws RemoteException {
        if (thread != null){
            if (thread.isAlive())
                try {
                    throw new Exception("Job is still running.");
                } catch (Exception e) {
                    throw new RemoteException();
                }
        }
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
        setStatus(LongTaskStatus.Running);
        setChanged();
    }
}
