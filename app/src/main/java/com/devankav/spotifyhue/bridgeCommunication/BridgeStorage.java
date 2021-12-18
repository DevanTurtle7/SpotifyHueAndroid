package com.devankav.spotifyhue.bridgeCommunication;

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
        return storage.get(id);
    }
}
