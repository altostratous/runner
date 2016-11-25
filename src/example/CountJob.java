package example;

import util.LongTask;
import util.LongTaskStatus;

import java.io.Serializable;

/**
 * Created this is an example of a long task that can be run using
 * Runner project. You can create classes that extend LongTask and
 * after compiling add them to the server to be run.
 */
public class CountJob extends LongTask {
    {
        setSupportsCancellation(true);
        setSupportsPause(true);
        setState(new CountJobState());
        setDisplayName("CountJob");
    }

    /**
     * Terminates the task, by cancelling it.
     */
    @Override
    public void terminate() {
        try {
            cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (getStatus() == LongTaskStatus.Running  ){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A serializable inner class used to store the state of the task,
     * so it can be guaranteed by the RunnerServer
     */
    private static class CountJobState implements Serializable
    {
        public Integer number = 0;
    }


    /**
     * The main operation is done here. Here is a sample. It is a counter that counts
     * to 100 with 500 milliseconds interval. Remember to set status just like bellow
     * as it is changed otherwise the task is not considered as completed or ...
     * In such case the task can only be terminated.
     */
    @Override
    public void run() {
        // main loop
        while (((CountJobState)getState()).number < 100)
        {
            ((CountJobState)getState()).number++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(progress);
            progress = ((CountJobState)getState()).number / 100.0;
            setChanged();
            if (pleaseBreak())
                break;
        }

        // set the status
        if (pleasePause){
            setStatus(LongTaskStatus.Paused);
        }
        if (pleaseCancel){
            setStatus(LongTaskStatus.Cancelled);
            progress = 0;
            setState(new CountJobState());
        }
        if (!pleaseBreak())
        {
            setStatus(LongTaskStatus.Completed);
            setState(new CountJobState());
        }
        setChanged();
    }
}
