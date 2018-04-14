package me.grimepp;

import me.grimepp.system.Config;
import me.grimepp.system.Default;
import org.bukkit.plugin.java.JavaPlugin;

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
        cm = new Config(getConfig());
    }

    public Config getCM() {
        return this.cm;
    }
}
