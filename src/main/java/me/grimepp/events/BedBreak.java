package me.grimepp.events;

import me.grimepp.system.Game;
import me.grimepp.system.GameSession;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BedBreak implements Listener {
    @EventHandler
    public void onBedBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (Game.isInGame(player)) {
            Block block = e.getBlock();
            Material type = block.getType();
            if (type.equals(Material.SANDSTONE))
                return;
            if (!type.equals(Material.BED)){
                e.setCancelled(true);
                return;
            }
            Game game = Game.getGame(player);
            GameSession session =game.getSession();
            if (game.getBluebed().getLocation().distance(block.getLocation()) < 2 && session.getPlayer(Color.BLUE).getUniqueId() != player.getUniqueId()) {
                session.win(Color.BLUE);
                return;
            }else if (game.getRedbed().getLocation().distance(block.getLocation()) < 2 && session.getPlayer(Color.RED).getUniqueId() != player.getUniqueId()) {
                session.win(Color.RED);
                return;
            }
            e.setCancelled(true);
        }
    }
}
