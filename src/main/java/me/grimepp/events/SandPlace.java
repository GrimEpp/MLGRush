package me.grimepp.events;

import me.grimepp.system.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SandPlace implements Listener {
    @EventHandler
    public void onSandPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if (Game.isInGame(player) && block.getType().equals(Material.SANDSTONE))
            Game.getGame(player).getSession().addBlock(block);
    }
}
