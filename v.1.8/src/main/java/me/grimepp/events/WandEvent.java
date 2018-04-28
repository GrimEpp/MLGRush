package me.grimepp.events;

import me.grimepp.system.Default;
import me.grimepp.system.Wand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WandEvent extends Default implements Listener {
    @EventHandler
    public void onWand(PlayerInteractEvent e) {
        Wand wand = Wand.get(e.getPlayer());
        ItemStack item = e.getPlayer().getInventory().getItemInHand();
        if (e.getClickedBlock() != null && item != null&& item.hasItemMeta() && Objects.equals(item.getItemMeta().getDisplayName(), Wand.getWand().getItemMeta().getDisplayName()) && e.getPlayer().hasPermission((String) getConfig().get("settings.adminPermission"))) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getPlayer().isSneaking()) {
                wand.setBed1(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(getConfig().getMessage("messages.wand.bed1"));
            }
            else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getPlayer().isSneaking()) {
                wand.setBed2(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(getConfig().getMessage("messages.wand.bed2"));
            }

            else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                wand.setL1(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(getConfig().getMessage("messages.wand.box1"));
            }
            else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                wand.setL2(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(getConfig().getMessage("messages.wand.box2"));
            }
            e.setCancelled(true);
        }
    }
}
