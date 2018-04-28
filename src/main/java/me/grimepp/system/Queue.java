package me.grimepp.system;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queue {
    private final Game game;
    private List<Player> players;
    private static Map<Player, Queue> waiting;
    static {
        waiting = new HashMap<>();
    }
    Queue(Game game) {
        this.game = game;
        players = new ArrayList<>();
    }

    public static void clear(Player player) {
        if (waiting.containsKey(player))
            waiting.get(player).removePlayer(player);
    }

    public void addPlayer(Player p) {
        players.add(p);
        waiting.put(p, this);
        if (players.size() >= 2) {
            game.start();
        }
    }

    public boolean canTakeMore() {
        return players.size() != 2;
    }

    public void removePlayer(Player p) {
        players.remove(p);
        waiting.remove(p);
    }

    public static boolean isWaiting(Player p) {
        return waiting.containsKey(p);
    }

    public List<Player> getPlayers() {
        return players;
    }

    private Game getGame() {
        return game;
    }

    public void empty() {
        players.forEach(waiting::remove);
        players.clear();
    }
}
