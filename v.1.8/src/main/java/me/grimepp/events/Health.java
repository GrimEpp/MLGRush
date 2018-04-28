package me.grimepp.events;

import me.grimepp.system.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Health implements Listener {
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && Game.isInGame((Player) e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && Game.isInGame((Player) e.getEntity()) && e.getCause() == EntityDamageEvent.DamageCause.FALL)
            e.setCancelled(true);
    }
}
