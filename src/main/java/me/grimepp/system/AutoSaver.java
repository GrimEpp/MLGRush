package me.grimepp.system;

import jdk.nashorn.internal.objects.NativeUint8Array;
import me.grimepp.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AutoSaver {
    private static FileConfiguration games;
    private static FileConfiguration stats;
    private static File file = new File(MLGRush.getInstance().getDataFolder(), "games.yml");
    private static File file1 = new File(MLGRush.getInstance().getDataFolder(), "stats.yml");
    public static void setConfig(FileConfiguration config, FileConfiguration config1) {
        games = config;
        stats = config1;
    }

    public static void saveGame(Game game) {
        String path = "games." + game.getId() + ".";
        games.set(path + "bed.red", game.getRedbed());
        games.set(path + "bed.blue", game.getBluebed());
        games.set(path + "spawn.red", game.getSpawnRed());
        games.set(path + "spawn.blue", game.getSpawnBlue());
        games.set(path + "cube.1", game.getCube().getPoint1());
        games.set(path + "cube.2", game.getCube().getPoint2());
        try {
            games.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGame(int id) {
        games.set("games." + id, "");
    }

    public static void load() {
        ConfigurationSection game = games.getConfigurationSection("games");
        if (game != null) {
            game.getKeys(false).forEach(id -> {
                Location bedred = (Location) games.get("games." + id + ".bed.red");
                Location bedblue = (Location) games.get("games." + id + ".bed.blue");

                Location spawnred = (Location) games.get("games." + id + ".spawn.red");
                Location spawnblue = (Location) games.get("games." + id + ".spawn.blue");

                Location cube1 = (Location) games.get("games." + id + ".cube.1");
                Location cube2 = (Location) games.get("games." + id + ".cube.2");
                Game.createGame(new Cube(cube1, cube2), spawnblue, bedblue, spawnred, bedred, Integer.parseInt(id));
            });
        }
        ConfigurationSection st = stats.getConfigurationSection("stats");
        if (st != null) {
            st.getKeys(false).forEach(s -> {
                UUID uuid = UUID.fromString(s);
                Stats stats = Stats.getStats(uuid);
                stats.setLosses(AutoSaver.stats.getInt("stats." + s + ".losses"));
                stats.setWins(AutoSaver.stats.getInt("stats." + s + ".wins"));
            });
            Bukkit.broadcastMessage("works");
        }
    }

    public static void saveStats(UUID uuid) {
        Stats statss = Stats.getStats(uuid);
        stats.set("stats." + uuid + ".wins", statss.getWins());
        stats.set("stats." + uuid + ".losses", statss.getLosses());
        try {
            stats.save(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}