package com.devankav.spotifyhue.bridgeCommunication;

import android.content.Context;

import com.devankav.spotifyhue.bridgeCommunication.Bridge;

import java.util.HashMap;

public class BridgeStorage {

    //TODO: REFACTOR THE WHOLE APP TO USE THIS AS BRIDGE STORAGE
    private static HashMap<String, Bridge> storage = new HashMap<>();

    public static void addBridge(String id, Bridge bridge) {
        if (!storage.containsKey(id)) {
            storage.put(id, bridge);
        }
    }

    public static Bridge getBridge(String id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        } else {
            throw new BridgeNotFoundException();
        }
    }

    public static Bridge getBridgeSafe(String id, String ipAddress, String username, Context context) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        } else {
            Bridge bridge = new Bridge(ipAddress, id, username, context);
            addBridge(id, bridge);

            return bridge;
        }
    }
}
