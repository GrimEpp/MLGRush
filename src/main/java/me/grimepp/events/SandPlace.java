package me.grimepp.events;

import me.grimepp.system.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SandPlace implements Listener {
    @EventHandler
    public void onSandPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        boolean inGame = Game.isInGame(player);
        Game game = Game.getGame(player);
        if (inGame && game.getCube().contains(block.getLocation())) {
            e.setCancelled(true);
            return;
        }
        if (inGame && block.getType().equals(Material.SANDSTONE))
            game.getSession().addBlock(block);

    }
}
