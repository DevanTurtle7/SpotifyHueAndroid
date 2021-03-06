/**
 * An abstract class that supports observers. Classes that derive this class
 * should have asynchronous events that happen more than once.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.observers;

import java.util.HashSet;
import java.util.Set;

public abstract class Observable<Observer> {

    protected Set<Observer> observers;

    public Observable() {
        this.observers = new HashSet<>();
    }

    /**
     * Registers an observer to be notified whenever there is an update
     * @param observer The observer being registered
     */
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Deregisters an observer
     * @param observer The observer being deregistered
     */
    public void deregisterObserver(Observer observer) {
        this.observers.remove(observer);
    }
}
