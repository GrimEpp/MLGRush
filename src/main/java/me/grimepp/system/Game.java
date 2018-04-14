package me.grimepp.system;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Default {
    private static Map<Integer, Game> gameMap;
    private GameSession gameSession;

    public static Game createGame(Cube cube) {
        Game game = new Game(cube);
        if (gameMap == null)
            gameMap = new ConcurrentHashMap<>();
        gameMap.put(gameMap.size(), game);
        return game;
    }
    private Cube cube;
    private Game(Cube cube) {
this.cube = cube;
queue = new Queue();
    }

    private Queue queue;

    public static boolean isInGame(Player p) {
    }

    public void start() {
        if (queue.canTakeMore()) {
            if (!(Boolean)getConfig().get("settings.bypasslimit")) {
                return;
            }
        }
        this.gameSession = new GameSession();
    }

    public Queue getQueue() {
        return queue;
    }
}
 class Queue {
    private Collection<? super Player> players;
    private static List<Player> waiting;
    static {
        waiting = new ArrayList<>();
    }
     Queue() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player p) {
        players.add(p);
        waiting.add(p);
    }

    public boolean canTakeMore() {
        return players.size() != 2;
    }

    public void removePlayer(Player p) {
        players.remove(p);
        waiting.remove(p);
    }

    public static boolean isWaiting(Player p) {
        return waiting.contains(p);
    }

    public Collection<? super Player> getPlayers() {
        return players;
    }
}