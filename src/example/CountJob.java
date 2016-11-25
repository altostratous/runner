package example;

import util.LongTask;
import util.LongTaskStatus;

import javax.management.ListenerNotFoundException;
import java.io.Serializable;

/**
 * Created by HP PC on 11/25/2016.
 */
public class CountJob extends LongTask {
    {
        setSupportsCancellation(true);
        setSupportsPause(true);
        setState(new CountJobState());
        setDisplayName("CountJob");
    }

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

    private static class CountJobState implements Serializable
    {
        public Integer number = 0;
    }

    @Override
    public void run() {
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
