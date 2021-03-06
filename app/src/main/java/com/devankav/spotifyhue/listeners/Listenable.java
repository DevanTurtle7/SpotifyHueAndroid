/**
 * An abstract class that supports listeners. Has events that happen exactly once that
 * can be listened to.c
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.listeners;

import java.util.HashSet;
import java.util.Set;

public abstract class Listenable<Listener> {

    protected Set<Listener> listeners;
    private boolean finished;

    public Listenable() {
        this.listeners = new HashSet<>();
        this.finished = false;
    }

    /**
     * Registers a listener to be notified when the event is finished
     * @param listener The listener being registered
     */
    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Deregisters a listener
     * @param listener The listener being deregistered
     */
    public void deregisterListener(Listener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Sets this object as finished
     */
    protected void finish() {
        if (!this.isFinished()) {
            this.finished = true;
        } else {
            throw new ListenerFinishedException("Attempted to finish a listener that is already finished.");
        }
    }

    /**
     * An accessor for finished
     * @return Whether or not this object is finished
     */
    public boolean isFinished() {
        return this.finished;
    }
}
