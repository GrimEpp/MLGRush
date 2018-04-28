package me.grimepp.system;

import com.google.common.collect.ImmutableCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class  Stats extends Default {
    private static Map<UUID, Stats> statsMap;
    static {
        statsMap = new ConcurrentHashMap<>();
    }

    private final UUID uuid;

    private int wins, losses;
    private Stats(UUID uuid) {
        statsMap.put(uuid,this);
        wins = 0;
        losses = 0;
        this.uuid = uuid;
    }
    public void win() {
        wins++;
        AutoSaver.saveStats(uuid);
    }
    public void loose() {
        losses++;
        AutoSaver.saveStats(uuid);
    }
    public static Stats getStats(UUID uuid) {
       if (statsMap.containsKey(uuid)) {
           return statsMap.get(uuid);
       } else {
           return new Stats(uuid);
       }
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getWins() {
        return wins;
    }
}
