package com.devankav.spotifyhue.listeners;

public class ListenerFinishedException extends RuntimeException {

    public ListenerFinishedException() {
        super("Attempted to update listenable object after it finished.");
    }

    public ListenerFinishedException(String message) {
        super(message);
    }
}
