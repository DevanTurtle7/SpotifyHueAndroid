/**
 * An abstract class that supports listeners.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.listeners;

import java.util.HashSet;
import java.util.Set;

public abstract class Listenable<Listener> {
    protected Set<Listener> listeners;

    public Listenable() {
        this.listeners = new HashSet<>();
    }

    /**
     * Registers a listener to be notified when the event is finished
     * @param listener The listener being registered
     */
    public void registerObserver(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Deregisters a listener
     * @param listener The listener being deregistered
     */
    public void deregisterObserver(Listener listener) {
        this.listeners.remove(listener);
    }
}
