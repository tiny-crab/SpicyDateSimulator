package com.example.evan.spicydatingsim;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by evan on 11/26/2016.
 */

public class SDSGameSave {
    private HashMap<String, Boolean> flags;

    private String curNode = null;
    public SDSGameSave() {

    }

    public Set<String> getAllFlags() {
        return flags.keySet();
    }

    public boolean hasFlag(String key) {
        return flags.containsKey(key);
    }

    public void setFlag(String key) {
        flags.put(key, true);
    }

    public String getCurNode() {
        return curNode;
    }

    public void setCurNode(String node) {
        curNode = node;
    }
}
