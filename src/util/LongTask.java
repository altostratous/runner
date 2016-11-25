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
    /**
     * Cancellation flag
     */
    protected boolean pleaseCancel;
    /**
     * Pause flag
     */
    protected boolean pleasePause;

    // the state to resume
    private Object state;

    /**
     * Set pause flag
     * @throws Exception
     */
    public void pause() throws Exception {
        if (!getSupportsPause())
            throw new Exception("Task doesn't support pause.");
        pleasePause = true;
    }

    /**
     * Check if one of pause or cancellation flags are up
     * @return
     */
    protected boolean pleaseBreak(){
        return pleaseCancel || pleasePause;
    }

    /**
     * Gets the state
     * @return
     */
    protected Object getState() {
        return state;
    }

    /**
     * Sets the state
     * @param state
     */
    protected void setState(Object state) {
        this.state = state;
    }

    /**
     * This field is the progress reported to the clients
     */
    protected double progress;

    /**
     * Sets cancellation flag
     * @throws Exception
     */
    public void cancel() throws Exception {
        if (!getSupportsCancellation()){
            throw new Exception("Task doen't support cancellation.");
        }
        pleaseCancel = true;
    }


    private boolean supportsPause = true;
    private boolean supportsCancellation = true;

    /**
     * if supports pause or not
     * @param supportsPause
     */
    protected void setSupportsPause(boolean supportsPause){
        this.supportsPause = supportsPause;
    }

    /**
     * if Supports cancellation or not
     * @param supportsCancellation
     */
    protected void setSupportsCancellation(boolean supportsCancellation){
        this.supportsCancellation = supportsCancellation;
    }

    /**
     * supports pause or not
     * @return
     */
    public boolean getSupportsPause() {
        return supportsPause;
    }


    /**
     * supports cancellation or not
     * @return
     */
    public boolean getSupportsCancellation() {
        return supportsCancellation;
    }

    private String displayName;

    /**
     * Get the task display name
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets task display name
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private LongTaskStatus status;

    /**
     * Task status
     * @return
     */
    public LongTaskStatus getStatus() {
        return status;
    }

    /**
     * To set the task status
     * @param status
     */
    protected void setStatus(LongTaskStatus status){
        this.status = status;
    }

    /**
     * gets progress
     * @return
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Initializes a task
     */
    public LongTask(){
        setChanged();
        setStatus(LongTaskStatus.NotStartedYet);
    }

    /**
     * Should terminate the thread and release resources
     */
    public abstract void terminate();

    private transient Thread thread;

    /**
     * Restarts the running thread completely
     * @throws RemoteException
     */
    public void restart() throws RemoteException {
        if (thread != null){
            if (thread.isAlive())
                try {
                    throw new Exception("Job is still running.");
                } catch (Exception e) {
                    throw new RemoteException();
                }
        }
        pleaseCancel = pleasePause = false;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
        setStatus(LongTaskStatus.Running);
        setChanged();
    }
}
