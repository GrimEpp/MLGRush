package me.grimepp.system;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class GameSession extends Default {
    private final BlockManager blockManager;
    private final Game game;
    private Player team1, team2;
    private int points1, points2;
    GameSession(Game game, List<Player> players) {
        team1 = players.get(ThreadLocalRandom.current().nextInt(0, 1));
        team2 = players.get(0) == team1 ? players.get(1) : team1;
        points1 = 0;
        points2 = 0;
        this.game = game;
        this.blockManager = new BlockManager();
    }


    public void win(Color color) {
        if (color == Color.BLUE) {
            points1++;
           Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"title " +team1.getName() + " title"+  getConfig().getColouredString("titles.round.winner.title", new MapBuilder().add(
                    "%winner%", team1.getName(),
                    "%looser%", team2.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
           Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " +team1.getName() + " subtitle"+ getConfig().getColouredString("titles.round.winner.subtitle", new MapBuilder().add(
                   "%winner%", team1.getName(),
                   "%looser%", team2.getName(),
                   "%pointsred%", String.valueOf(points2),
                   "%pointsblue%", String.valueOf(points1)
           ).getMap()));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"title " +team2.getName() + " title"+  getConfig().getColouredString("titles.round.looser.title", new MapBuilder().add(
                    "%winner%", team1.getName(),
                    "%looser%", team2.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " +team2.getName() + " subtitle"+ getConfig().getColouredString("titles.round.looser.subtitle", new MapBuilder().add(
                    "%winner%", team1.getName(),
                    "%looser%", team2.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
        } else {
            points2++;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"title " +team2.getName() + " title"+  getConfig().getColouredString("titles.round.winner.title", new MapBuilder().add(
                    "%winner%", team2.getName(),
                    "%looser%", team1.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " +team2.getName() + " subtitle"+ getConfig().getColouredString("titles.round.winner.subtitle", new MapBuilder().add(
                    "%winner%", team2.getName(),
                    "%looser%", team1.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"title " +team1.getName() + " title"+  getConfig().getColouredString("titles.round.looser.title", new MapBuilder().add(
                    "%winner%", team2.getName(),
                    "%looser%", team1.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " +team1.getName() + " subtitle"+ getConfig().getColouredString("titles.round.looser.subtitle", new MapBuilder().add(
                    "%winner%", team2.getName(),
                    "%looser%", team1.getName(),
                    "%pointsred%", String.valueOf(points2),
                    "%pointsblue%", String.valueOf(points1)
            ).getMap()));
        }
        team1.teleport(game.getSpawnBlue());
        team2.teleport(game.getSpawnRed());
        if (points1 == (int) getConfig().get("settings.runder")) {
            game.win(new GameData(team1, team2, points1, points2));
        } else if (points2 == (int) getConfig().get("settings.runder")){
            game.win(new GameData(team2, team1, points2, points1));
        }
        blockManager.blocks.forEach(b->b.setType(Material.AIR));
    }

    public void addBlock(Block b) {
        blockManager.blocks.add(b);
    }

    public Player getPlayer(Color blue) {
        if (blue.equals(Color.BLUE))
            return team1;
        else
            return team2;
    }

    public Player getOpposite(Player player) {
        return team1 == player ? team2 : team1;
    }

    public Color getColor(Player player) {
        return player == team1 ? Color.BLUE : Color.RED;
    }

    public int getPoints(Color color) {
        return color == Color.BLUE ? points1 : points2;
    }

    public static class GameData {
        int pointsRed;
        Player looser;
        Player winner;
        int pointsBlue;

        public GameData(Player team1, Player team2, int points1, int points2) {
            winner = team1;
            looser = team2;
            pointsBlue = points1;
            pointsRed = points2;
        }
    }

    private class BlockManager {
        List<Block> blocks;
        BlockManager() {
            blocks = new ArrayList<>();
        }
    }

}
