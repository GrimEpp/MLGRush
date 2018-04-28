package me.grimepp.events;

import me.grimepp.MLGRush;
import me.grimepp.system.Game;
import me.grimepp.system.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Bed;
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
            if (!(block.getState() instanceof Bed)){
                e.setCancelled(true);
                return;
            }
            Game game = Game.getGame(player);
            GameSession session =game.getSession();
            e.setCancelled(true);
            if (game.getBluebed().distance(block.getLocation()) < 2 && session.getPlayer(Color.BLUE).getUniqueId() != player.getUniqueId()) {
                session.win(Color.RED);
            }else if (game.getRedbed().distance(block.getLocation()) < 2 && session.getPlayer(Color.RED).getUniqueId() != player.getUniqueId()) {
                session.win(Color.BLUE);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(MLGRush.getInstance(), ()-> e.getBlock().getState().update(true));
        }
    }
}