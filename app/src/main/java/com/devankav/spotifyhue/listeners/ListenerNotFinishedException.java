/**
 * An exception that is generally thrown when an attempt to access data is made before a listener
 * finishes.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.listeners;

public class ListenerNotFinishedException extends RuntimeException {

    public ListenerNotFinishedException() {
        super("Attempted to access listenable object before it was finished.");
    }

    public ListenerNotFinishedException(String message) {
        super(message);
    }
}