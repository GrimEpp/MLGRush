package me.grimepp.system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Stats {
    private static Map<UUID, Stats> statsMap;
    static {
        statsMap = new ConcurrentHashMap<>();
    }

    private final UUID uuid;

    private int wins, losses;
    private Stats(UUID uuid) {
        this.uuid = uuid;
        statsMap.put(uuid,this);
    }
    public void win() {
        wins++;
    }
    public void loose() {
        losses++;
    }
    public Stats getStats(UUID uuid) {
       if (statsMap.containsKey(uuid)) {
           return statsMap.get(uuid);
       } else {
           return new Stats(uuid);
       }
    }

    public int getLosses() {
        return losses;
    }

    public int getWins() {
        return wins;
    }
}
