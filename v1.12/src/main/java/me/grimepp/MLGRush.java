package me.grimepp;

import me.grimepp.commands.GameCommand;
import me.grimepp.commands.StatsCommand;
import me.grimepp.events.*;
import me.grimepp.system.AutoSaver;
import me.grimepp.system.Config;
import me.grimepp.system.Default;
import me.grimepp.system.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MLGRush extends JavaPlugin {
    private static MLGRush instance;
    private Config cm;

    public static MLGRush getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        prepareConfig();
        AutoSaver.load();
        registerEvents();
        registerCommands();
    }
    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BedBreak(), this);
        pm.registerEvents(new Quit(), this);
        pm.registerEvents(new SandPlace(), this);
        pm.registerEvents(new Freeze(), this);
        pm.registerEvents(new WandEvent(), this);
        pm.registerEvents(new Join(), this);
        pm.registerEvents(new Health(), this);
    }

    private void registerCommands() {
        getCommand("game").setExecutor(new GameCommand());
        getCommand("stats").setExecutor(new StatsCommand());
    }

    private void prepareConfig() {
        saveDefaultConfig();


        File file = new File(getDataFolder(),"messages.yml");
        if (!file.exists())
            saveResource("messages.yml", false);
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        cm = new Config(getConfig(), configuration);


        File file1 = new File(getDataFolder(),"games.yml");
        if (!file1.exists()) {
          try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration configuration1 = new YamlConfiguration();
        try {
            configuration1.load(file1);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        File file2 = new File(getDataFolder(),"stats.yml");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration configuration2 = new YamlConfiguration();
        try {
            configuration2.load(file2);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        AutoSaver.setConfig(configuration1, configuration2);
    }

    public Config getCM() {
        return this.cm;
    }
}
