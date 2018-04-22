package me.grimepp.system;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;
import java.util.Collection;

public class Freeze implements Listener {
    static Collection<Player> players;
    static {
        players = new ArrayList<>();
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (players.contains(e.getPlayer()))
            e.setCancelled(true);
    }
}
