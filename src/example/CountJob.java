package example;

import util.LongTask;

/**
 * Created by HP PC on 11/25/2016.
 */
public class CountJob extends LongTask {
    {
        setSupportsCancellation(true);
        setSupportsPause(true);
        setState(new CountJob());
        setDisplayName("CountJob");
    }

    private static class CountJobState
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
