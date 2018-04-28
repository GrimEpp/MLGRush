package me.grimepp.events;

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
            Bukkit.broadcastMessage("yes");
            Material type = block.getType();
            if (type.equals(Material.SANDSTONE))
                return;
            if (!(block.getState() instanceof Bed)){
                e.setCancelled(true);
                return;
            }
            Bukkit.broadcastMessage("youwat");
            Game game = Game.getGame(player);
            GameSession session =game.getSession();
            if (game.getBluebed().distance(block.getLocation()) < 2 && session.getPlayer(Color.BLUE).getUniqueId() != player.getUniqueId()) {
                session.win(Color.RED);
                Bukkit.broadcastMessage("youwat1");
                e.setCancelled(true);
                return;
            }else if (game.getRedbed().distance(block.getLocation()) < 2 && session.getPlayer(Color.RED).getUniqueId() != player.getUniqueId()) {
                session.win(Color.BLUE);
                Bukkit.broadcastMessage("youwat2");
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
        }
    }
}
