package com.devankav.spotifyhue.bridgeCommunication;

import com.devankav.spotifyhue.bridgeConnection.BridgeResult;
import com.devankav.spotifyhue.listeners.DiscoveryListener;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.listeners.Listenable;
import com.devankav.spotifyhue.listeners.ListenerFinishedException;
import com.devankav.spotifyhue.listeners.ListenerNotFinishedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LightGroup extends Listenable<LightsListener> {
    List<Light> lights;

    public LightGroup() {
        this.lights = new ArrayList<>();
    }

    public void updateLights(List<Light> lightList) {
        if (!this.isFinished()) {
            this.finish();
            this.lights.addAll(lightList);
            notifyListeners(); // Notify the listeners
        } else {
            throw new ListenerFinishedException();
        }
    }

    public List<Light> getLights() {
        if (this.isFinished()) {
            return this.lights;
        } else {
            throw new ListenerNotFinishedException();
        }
    }

    private void notifyListeners() {
        for (LightsListener listener : listeners) { // Iterate over each listener
            listener.finished(this); // Notify the listener of the results
        }
    }

    public boolean hasResults() {
        if (this.isFinished()) {
            return !this.lights.isEmpty();
        } else {
            throw new ListenerNotFinishedException();
        }
    }
}
