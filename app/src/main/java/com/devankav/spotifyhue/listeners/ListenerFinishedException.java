/**
 * An exception that is generally thrown when an attempt to update data is made after a listener
 * has finished.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.listeners;

public class ListenerFinishedException extends RuntimeException {

    public ListenerFinishedException() {
        super("Attempted to update listenable object after it finished.");
    }

    public ListenerFinishedException(String message) {
        super(message);
    }
}
