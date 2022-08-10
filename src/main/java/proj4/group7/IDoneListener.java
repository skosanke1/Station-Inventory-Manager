package proj4.group7;

/**
 * This interface is used to communicate between two classes
 * to promote loosely coupling
 */

// Inform another class that you are done with a task
public interface IDoneListener {
    public void done();
}