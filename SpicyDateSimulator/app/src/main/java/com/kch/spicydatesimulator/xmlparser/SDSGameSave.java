package com.kch.spicydatesimulator.xmlparser;

import java.util.HashMap;
import java.util.Set;


public class SDSGameSave {
    private HashMap<String, Boolean> flags;

    private String curNode = null;
    public SDSGameSave() {
        flags = new HashMap<>();
    }

    /**
     * Return a set of all of the flags currently set in the game map
     * @return Unmodifiable reference to the set of keys in the game map
     */
    public Set<String> getAllFlags() {
        return flags.keySet();
    }

    /**
     * Returns the game map, used by GameNodes to know which responses and
     * choices to show
     * @return Unmodifiable reference to the game map
     */
    public HashMap<String, Boolean> getFlagMap() {
        return flags;
    }

    /**
     * Updates the current node in the game, used for saving state
     * @param nodeId string id of the new current node
     */
    public void updateCurNode(String nodeId) {
        curNode = nodeId;
    }

    /**
     * Check for the key in the game map
     * @param key key to check
     * @return true if it is in the map, false otherwise
     */
    public boolean hasFlag(String key) {
        return flags.containsKey(key);
    }

    public void setFlag(String key) {
        flags.put(key, true);
    }

    public String getCurNode() {
        return curNode;
    }
}
