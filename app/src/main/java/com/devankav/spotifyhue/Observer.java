/**
 * A functional interface that acts as a generic observer
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

public interface Observer<T> {
    public abstract void notifyObserver(T updated);
}
