package me.grimepp;

import me.grimepp.system.Config;
import me.grimepp.system.Default;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    }

    private void prepareConfig() {
        saveDefaultConfig();
        File file = new File(getDataFolder(),"messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveResource("messages.yml", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        cm = new Config(getConfig(), configuration);
    }

    public Config getCM() {
        return this.cm;
    }
}
