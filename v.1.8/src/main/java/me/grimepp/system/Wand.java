package me.grimepp.system;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Wand extends Default {
    private static ItemStack wand;
    private static Map<Player, Wand> wandMap;
    static {
        wandMap = new HashMap<>();
        wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName(getConfigs().getColouredConfigString("settings.wandname"));
        wand.setItemMeta(meta);
    }
    private Location cube1;
    private Location cube2;
    private Location spawn1;
    private Location spawn2;
    private Location bed1, bed2;
    private Wand(Player p) {
        wandMap.put(p, this);
    }

    public static Wand get(Player p) {
      return   wandMap.containsKey(p) ? wandMap.get(p) : new Wand(p);
    }
    public static ItemStack getWand() {
        return wand;
    }

    public boolean isValid() {
        Bukkit.broadcastMessage(String.valueOf(cube1 == null) + "    1     " + String.valueOf(cube2 == null) + "       1     " + String.valueOf(spawn1 == null) + "    1     " + String.valueOf(spawn2 == null) + "       1     " + String.valueOf(bed1 == null) + "    1     " + String.valueOf(bed2 == null) + "       1     ");
        return cube1 != null && cube2 != null && cube1.getWorld().equals(cube2.getWorld()) && spawn1 != null && spawn2 != null && spawn1.getWorld().equals(spawn2.getWorld()) && bed1 != null && bed2 != null && bed1.getWorld().equals(bed2.getWorld()) ;
    }

    public Location getL2() {
        return cube2;
    }

    public void setL2(Location l2) {
        this.cube2 = l2;
    }

    public Location getL1() {
        return cube1;
    }

    public void setL1(Location l1) {
        this.cube1 = l1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public Location getBed1() {
        return bed1;
    }

    public Location getBed2() {
        return bed2;
    }

    public void setBed1(Location bed1) {
        this.bed1 = bed1;
    }

    public void setBed2(Location bed2) {
        this.bed2 = bed2;
    }
}
