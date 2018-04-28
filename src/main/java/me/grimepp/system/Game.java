package me.grimepp.system;

import me.grimepp.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Default {
    private static Map<Integer, Game> gameMap;
    private GameSession gameSession;
    private static Map<Player, Game> players;
    static {
        players = new ConcurrentHashMap<>();
        gameMap = new ConcurrentHashMap<>();
    }

    private GameStatus status;
    private int id;

    public static Game createGame(Cube cube, Location spawnblue, Location bedblue, Location spawnred, Location bedred) {
        Game game = new Game(cube);
        if (gameMap == null)
            gameMap = new ConcurrentHashMap<>();
        game.id = gameMap.size() + 1;
        gameMap.put(game.id, game);
        game.redbed = bedred;
        game.bluebed = bedblue;
        game.spawnblue = spawnblue;
        game.spawnred = spawnred;
        game.status = GameStatus.WAITING;
        AutoSaver.saveGame(game);
        return game;
    }


    public static void createGame(Cube cube, Location spawnblue, Location bedblue, Location spawnred, Location bedred, int i) {
        Game game = new Game(cube);
        if (gameMap == null)
            gameMap = new ConcurrentHashMap<>();
        game.id = i;
        gameMap.put(game.id, game);
        game.redbed = bedred;
        game.bluebed = bedblue;
        game.spawnblue = spawnblue;
        game.spawnred = spawnred;
        game.status = GameStatus.WAITING;
        AutoSaver.saveGame(game);
    }

    private Cube cube;
    private Location spawnblue, spawnred;
    private Location redbed, bluebed;
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

    public static void deleteGame(int i) {
        if (gameMap.get(i).getStatus() == GameStatus.INGAME) {
            gameMap.get(i).forceStop();
        }
        AutoSaver.deleteGame(i);
    }


    public void start() {
        status = GameStatus.INGAME;
        this.gameSession = new GameSession(this, queue.getPlayers());
        PlayerSession.store(gameSession.getPlayer(Color.RED));
        PlayerSession.store(gameSession.getPlayer(Color.BLUE));
        gameSession.getPlayer(Color.RED).teleport(spawnred);
        gameSession.getPlayer(Color.BLUE).teleport(spawnblue);
        Freeze.players.add(gameSession.getPlayer(Color.BLUE));
        players.put(gameSession.getPlayer(Color.RED), this);
        players.put(gameSession.getPlayer(Color.BLUE), this);
        ScoreBoardManager.reset(gameSession.getPlayer(Color.RED));
        ScoreBoardManager.reset(gameSession.getPlayer(Color.BLUE));
        Freeze.players.add(gameSession.getPlayer(Color.RED));
        queue.empty();
        giveItems();
        new BukkitRunnable(){
            int i = 5;
            Player blue = gameSession.getPlayer(Color.BLUE);
            Player red = gameSession.getPlayer(Color.RED);
            @Override
            public void run() {
                i--;
                if (status != GameStatus.INGAME) {
                    cancel();
                    return;
                }
                blue.sendTitle(getConfig().getColouredString("titles.start.title", new MapBuilder().add(
                        "%opponent%", red.getName(),
                        "%time%", String.valueOf(i)
                ).getMap()),getConfig().getColouredString("titles.start.subtitle", new MapBuilder().add(
                        "%opponent%", red.getName(),
                        "%time%", String.valueOf(i)
                ).getMap()), 10, 70, 20);

                red.sendTitle(getConfig().getColouredString("titles.start.title", new MapBuilder().add(
                        "%opponent%", blue.getName(),
                        "%time%", String.valueOf(i)
                ).getMap()),getConfig().getColouredString("titles.start.subtitle", new MapBuilder().add(
                        "%opponent%", blue.getName(),
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

    private void giveItems() {
        Player blue = gameSession.getPlayer(Color.BLUE);
        Player red = gameSession.getPlayer(Color.RED);
        blue.getInventory().clear();
        red.getInventory().clear();
        PlayerInventory blueInventory = blue.getInventory();

        PlayerInventory redInventory = red.getInventory();

        ItemStack knockbackstick = new ItemStack(Material.STICK);
        knockbackstick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);

        ItemStack sandstone = new ItemStack(Material.SANDSTONE, 64);

        ItemStack pickax = new ItemStack(Material.STONE_PICKAXE);
        pickax.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);

        ItemStack redHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) redHelmet.getItemMeta();
        meta.setColor(Color.RED);
        redHelmet.setItemMeta(meta);

        ItemStack redChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta1 = (LeatherArmorMeta) redChestPlate.getItemMeta();
        meta1.setColor(Color.RED);
        redChestPlate.setItemMeta(meta1);

        ItemStack redLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta2 = (LeatherArmorMeta) redLeggings.getItemMeta();
        meta2.setColor(Color.RED);
        redLeggings.setItemMeta(meta2);

        ItemStack redBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta3 = (LeatherArmorMeta) redBoots.getItemMeta();
        meta3.setColor(Color.RED);
        redBoots.setItemMeta(meta3);


        ItemStack blueHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta mmeta = (LeatherArmorMeta) blueHelmet.getItemMeta();
        mmeta.setColor(Color.BLUE);
        blueHelmet.setItemMeta(mmeta);

        ItemStack blueChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta mmeta1 = (LeatherArmorMeta) blueChestPlate.getItemMeta();
        mmeta1.setColor(Color.BLUE);
        blueChestPlate.setItemMeta(mmeta1);

        ItemStack blueLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta mmeta2 = (LeatherArmorMeta) blueLeggings.getItemMeta();
        mmeta2.setColor(Color.BLUE);
        blueLeggings.setItemMeta(mmeta2);

        ItemStack blueBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta mmeta3 = (LeatherArmorMeta) blueBoots.getItemMeta();
        mmeta3.setColor(Color.BLUE);
        blueBoots.setItemMeta(mmeta3);


        blueInventory.setHelmet(blueHelmet);
        blueInventory.setChestplate(blueChestPlate);
        blueInventory.setLeggings(blueLeggings);
        blueInventory.setBoots(blueBoots);

        redInventory.setHelmet(redHelmet);
        redInventory.setChestplate(redChestPlate);
        redInventory.setLeggings(redLeggings);
        redInventory.setBoots(redBoots);

        redInventory.setItem(0, knockbackstick);
        redInventory.setItem(2, pickax);
        redInventory.setItem(1, sandstone);

        blueInventory.setItem(0, knockbackstick);
        blueInventory.setItem(2, pickax);
        blueInventory.setItem(1, sandstone);
    }

    public Queue getQueue() {
        return queue;
    }

    public void win(GameSession.GameData gameData) {
        Player winner = gameData.winner;
        Player looser = gameData.looser;
        Stats.getStats(winner.getUniqueId()).win();
        Stats.getStats(looser.getUniqueId()).loose();
        players.remove(looser);
        players.remove(winner);
        if (getConfig().get("settings.broadcastwin"))
            Bukkit.broadcastMessage(getConfig().getMessage("messages.aftergame.broadcastmessage", new MapBuilder().add(
                    "%pointsblue%", String.valueOf(gameData.pointsBlue),
                    "%pointsred%", String.valueOf(gameData.pointsRed),
                    "%winner%", gameData.winner.getName(),
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
        PlayerSession.restore(looser);
        PlayerSession.restore(winner);
    }

    private void forceStop() {
        Player p1 = gameSession.getPlayer(Color.RED);
        Player p2 = gameSession.getPlayer(Color.BLUE);
        players.remove(p1);
        players.remove(p2);
        ScoreBoardManager.reset(p1);
        ScoreBoardManager.reset(p2);
        status = GameStatus.WAITING;
        PlayerSession.restore(p1);
        PlayerSession.restore(p2);
    }


    public GameSession getSession() {
        return gameSession;
    }

    public Location getBluebed() {
        return bluebed;
    }

    public Location getRedbed() {
        return redbed;
    }

    public Cube getCube() {
        return cube;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public int getId() {
        return this.id;
    }

    public Location getSpawnRed() {
        return spawnred;
    }

    public Location getSpawnBlue() {
        return spawnblue;
    }
}
