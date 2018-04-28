package me.grimepp.system;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private Map<String, String> map;
    public MapBuilder() {
        map = new HashMap<>();
    }
    public MapBuilder add(String... strings) {
        if (strings.length % 2 != 0)
            return this;
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], strings[++i]);
        }
        return this;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
