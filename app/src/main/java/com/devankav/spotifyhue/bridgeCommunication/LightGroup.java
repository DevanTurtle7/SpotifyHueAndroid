package com.devankav.spotifyhue.bridgeCommunication;

import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.listeners.Listenable;
import com.devankav.spotifyhue.listeners.ListenerFinishedException;

import java.util.HashSet;
import java.util.Set;

public class LightGroup extends Listenable<LightsListener> {
    Set<Light> lights;

    public LightGroup() {
        this.lights = new HashSet<>();
    }

    public void updateLights(Set<Light> lightList) {
        if (!this.isFinished()) {
            this.finish();
            this.lights.addAll(lightList);
            notifyListeners(); // Notify the listeners
        } else {
            throw new ListenerFinishedException();
        }
    }

    public Set<Light> getLights() {
        return this.lights;
    }

    private void notifyListeners() {
        for (LightsListener listener : listeners) { // Iterate over each listener
            listener.finished(this); // Notify the listener of the results
        }
    }

    public boolean hasResults() {
        return !this.lights.isEmpty();
    }
}
