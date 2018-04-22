package me.grimepp.system;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;

public class Config {
    private final FileConfiguration yml;
    private final FileConfiguration yml1;
    private boolean isHooked = Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI");
    public Config(FileConfiguration fileConfiguration, FileConfiguration configuration) {
        Objects.requireNonNull(fileConfiguration);
        Objects.requireNonNull(configuration);
    this.yml = fileConfiguration;
    this.yml1 = configuration;
    }




    public <T> T get(String path) {
        return (T) yml.get(path);
    }

    public ConfigurationSection getSection(String path) {
        return yml.getConfigurationSection(path);
    }



    public String getColouredString(String path) {
        return ChatColor.translateAlternateColorCodes('&', yml1.getString(path));
    }

    public String getColouredConfigString(String path) {
        return ChatColor.translateAlternateColorCodes('&', yml.getString(path));
    }
    public String getMessage(String path) {
        return getColouredConfigString(("settings.prefix") + " " + getColouredString(path));
    }
    public String getMessage(String path, Map<String, String> place) {
        return replaceM(place, getColouredString("settings.prefix") + " " + getColouredString(path));
    }

    private String replaceM(Map<String, String> placeholder, String text) {
        for (Map.Entry<String, String> entry : placeholder.entrySet()) {
            text = text.replaceAll(entry.getKey(), ChatColor.translateAlternateColorCodes('&', entry.getValue()));
        }
        return text;
    }

    public String getColouredString(String s, Map<String,String> map) {
        return ChatColor.translateAlternateColorCodes('&', replaceM(map, s));
    }

    public ConfigurationSection getFormat(String path) {
        return yml1.getConfigurationSection(path);
    }
}
