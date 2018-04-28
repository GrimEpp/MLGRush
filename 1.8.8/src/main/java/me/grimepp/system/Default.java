package me.grimepp.system;

import me.grimepp.MLGRush;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;


public class Default {
    protected Config getConfig() {
    return MLGRush.getInstance().getCM();
}
     static Config getConfigs() {return MLGRush.getInstance().getCM();}
}
