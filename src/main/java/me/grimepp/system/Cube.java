package me.grimepp.system;

import org.bukkit.Location;

public class Cube extends Default{
    private Location l1, l2;
    private int x1, z1, x2, z2;
    public Cube(Location l1, Location l2){
        if (l1.getWorld().equals(l2.getWorld()))
            throw new IllegalStateException("Worlds er ikke like!");
        this.l1 = l1;
        this.l2 = l2;
        x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        x1 = Math.min(l1.getBlockX(), l2.getBlockX());

        z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
    }

    public boolean contains(Location l) {
        return l.getBlockX() >= this.x1 && l.getBlockX() <= this.x2 && l.getBlockZ() >= this.z1 && l.getBlockZ() <= this.z2;
    }
}
