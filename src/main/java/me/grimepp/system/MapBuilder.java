package me.grimepp.system;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapBuilder {
    private Map<String, String> map;
    public MapBuilder() {
        map = new HashMap<>();
    }
    public MapBuilder add(String... strings) {
        if (strings.length % 2 != 0)
            return this;
        for (int i = 0; i <= strings.length; i++) {
            map.put(strings[i], strings[i++]);
        }
        return this;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
