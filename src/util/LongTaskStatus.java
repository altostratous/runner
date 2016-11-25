package util;

/**
 * Created by HP PC on 11/25/2016.
 */
public enum LongTaskStatus {
    Paused,
    Running,
    Cancelled,
    Completed,
    Crashed,
    NotStartedYet;

    @Override
    public String toString() {
        switch (this){
            case Paused:
                return "Paused";
            case Running:
                return "Running";
            case Cancelled:
                return "Cancelled";
            case Completed:
                return "Completed";
            case Crashed:
                return "Crashed";
            case NotStartedYet:
                return "Not started yet.";
        }
        return null;
    }
}
