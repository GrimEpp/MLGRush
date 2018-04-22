package me.grimepp.system;

import me.grimepp.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Default {
    private static Map<Integer, Game> gameMap;
    private GameSession gameSession;
    private static Map<Player, Game> players;
    static {
        players = new ConcurrentHashMap<>();
    }

    private GameStatus status;

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
queue = new Queue(this);
    }

    private Queue queue;

    public static boolean isInGame(Player p) {
        return players.containsKey(p);
    }

    public static Game getGame(Player player) {
        return players.get(player);
    }

    public static Game getGame(int i) {
        return gameMap.get(i);
    }

    public static Map<Integer, Game> getGames() {
        return gameMap;
    }

    public void start() {
        if (queue.canTakeMore()) {
            if (!(boolean)getConfig().get("settings.bypasslimit")) {
                return;
            }
        }
        status = GameStatus.INGAME;
        this.gameSession = new GameSession(this, queue.getPlayers());
        Freeze.players.add(gameSession.getPlayer(Color.BLUE));
        Freeze.players.add(gameSession.getPlayer(Color.RED));
        new BukkitRunnable(){
            int i = 5;
            Player blue = gameSession.getPlayer(Color.BLUE);
            Player red = gameSession.getPlayer(Color.RED);
            @Override
            public void run() {
                i--;
                blue.sendTitle(getConfig().getColouredString("titles.start.title", new MapBuilder().add(
                        "%opponent%", red.getName(),
                        "%time%", String.valueOf(i)
                ).getMap()),getConfig().getColouredString("titles.start.subtitle", new MapBuilder().add(
                        "%opponent%", red.getName(),
                        "%time%", String.valueOf(i)
                ).getMap()), 10, 70, 20);
                if (i <= 0)
                    cancel();
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                Freeze.players.remove(blue);
                Freeze.players.remove(red);
                super.cancel();
            }
        }.runTaskTimer(MLGRush.getInstance(), 0L, 20L);
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
        status = GameStatus.WAITING;
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

    public Cube getCube() {
        return cube;
    }

    public GameStatus getStatus() {
        return this.status;
    }
}
