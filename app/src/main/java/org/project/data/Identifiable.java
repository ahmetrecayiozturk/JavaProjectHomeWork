package org.project.data;

/**
 * This interface defines a contract for objects that can be identified by a unique ID.
 * Any class that implements this interface must provide an implementation for the getId() method.
 */
public interface Identifiable {
    /**
     * Method to get the unique identifier of the object.
     *
     * @return the unique identifier of the object as an Integer.
     */
    Integer getId();
}