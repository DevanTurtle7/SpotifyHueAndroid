package com.devankav.spotifyhue;

public interface Observer<T> {
    public abstract void notifyObserver(T updated);
}
