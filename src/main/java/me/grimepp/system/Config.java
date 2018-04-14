package me.grimepp.system;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config {
    private final FileConfiguration yml;

    public Config(FileConfiguration fileConfiguration) {
        Objects.requireNonNull(fileConfiguration);
    this.yml = fileConfiguration;
    }




    public <T> T get(String path) {
        return (T) yml.get(path);
    }

    public ConfigurationSection getSection(String path) {
        return yml.getConfigurationSection(path);
    }











    public String getColouredString(String path) {
        return ChatColor.translateAlternateColorCodes('&', get(path));
    }
    public String getMessage(String path) {
        return getColouredString("settings.prefix") + " " + getColouredString(path);
    }
}
