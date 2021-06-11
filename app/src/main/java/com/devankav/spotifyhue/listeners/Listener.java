/**
 * A functional interface that acts as a generic listener. Used to listen for asynchronous
 * events that happen exactly once.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.listeners;

public interface Listener<T> {
    public abstract void finished(T result);
}
