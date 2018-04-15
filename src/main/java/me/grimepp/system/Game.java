package me.grimepp.system;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Default {
    private static Map<Integer, Game> gameMap;
    private GameSession gameSession;
    private static Map<Player, Game> players;
    static {
        players = new ConcurrentHashMap<>();
    }

    public static Game createGame(Cube cube, Location spawnblue, Bed bedblue, Location spawnred, Bed bedred) {
        Game game = new Game(cube);
        if (gameMap == null)
            gameMap = new ConcurrentHashMap<>();
        gameMap.put(gameMap.size(), game);
        game.redbed = bedred;
        game.bluebed = bedblue;
        game.spawnblue = spawnblue;
        game.spawnred = spawnred;
        return game;
    }
    private Cube cube;
    private Location spawnblue, spawnred;
    private Bed redbed, bluebed;
    private Game(Cube cube) {
this.cube = cube;
queue = new Queue();
    }

    private Queue queue;

    public static boolean isInGame(Player p) {
        return players.containsKey(p);
    }

    public static Game getGame(Player player) {
        return players.get(player);
    }

    public void start() {
        if (queue.canTakeMore()) {
            if (!(boolean)getConfig().get("settings.bypasslimit")) {
                return;
            }
        }
        this.gameSession = new GameSession(this, queue.getPlayers());
    }

    public Queue getQueue() {
        return queue;
    }

    public void win(GameSession.GameData gameData) {
        Player winner = gameData.winner;
        Player looser = gameData.looser;
        Stats.getStats(winner.getUniqueId()).win();
        Stats.getStats(looser.getUniqueId()).win();
        players.remove(looser);
        players.remove(winner);
        if (getConfig().get("settings.broadcastwin"))
            Bukkit.broadcastMessage(getConfig().getMessage("messages.aftergame.broadcastmessage", new MapBuilder().add(
                    "%pointsblue%", String.valueOf(gameData.pointsBlue),
                    "%pointsred%", String.valueOf(gameData.pointsRed),
                    "%winnder%", gameData.winner.getName(),
                    "%looser%", gameData.looser.getName()
            ).getMap()));
        looser.sendMessage(getConfig().getMessage("messages.aftergame.loose",new MapBuilder()
                .add(
                        "%pointsblue%", String.valueOf(gameData.pointsBlue),
                        "%pointsred%", String.valueOf(gameData.pointsRed),
                        "%opponent%", winner.getName()

                ).getMap()));
        winner.sendMessage(getConfig().getMessage("messages.aftergame.win", new MapBuilder()
            .add(
                    "%pointsblue%", String.valueOf(gameData.pointsBlue),
                    "%pointsred%", String.valueOf(gameData.pointsRed),
                    "%opponent%", looser.getName()
            )
        .getMap()));
        ScoreBoardManager.reset(looser);
        ScoreBoardManager.reset(winner);
    }

    public GameSession getSession() {
        return gameSession;
    }

    public Bed getBluebed() {
        return bluebed;
    }

    public Bed getRedbed() {
        return redbed;
    }
}
 class Queue {
    private List<Player> players;
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

    public List<Player> getPlayers() {
        return players;
    }
}