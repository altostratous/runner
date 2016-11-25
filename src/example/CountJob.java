package example;

import util.LongTask;
import util.LongTaskStatus;

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
        }
    }
}
