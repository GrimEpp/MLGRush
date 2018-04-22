package me.grimepp.system;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Wand extends Default {
    private static ItemStack wand;
    static {
        wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName(getConfigs().getColouredConfigString("settings.wandname"));
        wand.setItemMeta(meta);
    }

    public static ItemStack getWand() {
        return wand;
    }
}
