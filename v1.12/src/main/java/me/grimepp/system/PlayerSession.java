package me.grimepp.system;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSession {

    private ItemStack[] inventory;
    private ItemStack[] armor;
    private ItemStack otherHand;
    private Location loc;

    private static Map<UUID, PlayerSession> map;
    static {
        map = new ConcurrentHashMap<>();
    }
    public static void store(Player p) {
        PlayerSession session = new PlayerSession();
        session.armor = p.getInventory().getArmorContents();
        session.otherHand = p.getInventory().getItemInOffHand();
        session.inventory = p.getInventory().getContents();
        session.loc = p.getLocation();
        map.put(p.getUniqueId(), session);
    }

    public static void restore(Player p) {
        map.get(p.getUniqueId()).sudo(p);
    }

    private void sudo(Player p) {
        p.getInventory().clear();
        p.getInventory().setContents(inventory);
        p.getInventory().setArmorContents(armor);
        p.getInventory().setItemInOffHand(otherHand);
        p.teleport(loc);
        map.remove(p.getUniqueId());
    }
}
