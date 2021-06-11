/**
 * A functional interface that acts as a generic observer. Used to listen for asynchronous
 * events that happen more than once.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.observers;

public interface Observer<T> {
    public abstract void notifyObserver(T updated);
}
