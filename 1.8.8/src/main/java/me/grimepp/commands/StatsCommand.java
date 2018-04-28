package me.grimepp.commands;

import me.grimepp.system.Default;
import me.grimepp.system.MapBuilder;
import me.grimepp.system.Stats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends Default implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(getConfig().getMessage("commands.game.ikkeSpiller"));
            return true;
        }
        Stats stats = Stats.getStats(((Player) commandSender).getUniqueId());
        getConfig().getSection("settings.stats").getKeys(false).forEach(k-> commandSender.sendMessage(getConfig().getColouredConfigString("settings.stats."+k, new MapBuilder().add("%wins%", String.valueOf(stats.getWins()), "%losses%", String.valueOf(stats.getLosses())).getMap())));
        return true;
    }
}
