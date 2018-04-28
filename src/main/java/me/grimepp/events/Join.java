package me.grimepp.events;

import me.grimepp.system.ScoreBoardManager;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ScoreBoardManager.reset(e.getPlayer());
    }
}
