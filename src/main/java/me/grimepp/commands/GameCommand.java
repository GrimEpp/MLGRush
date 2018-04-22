package me.grimepp.commands;

import me.grimepp.system.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GameCommand extends Default implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int length = args.length;
        if (length == 0) {
            commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            if (length == 1) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            String game = args[1];
            if (!isInt(game)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            Game game1 = Game.getGame(Integer.parseInt(game));
            if (game1 ==null) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
        if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            game1.getQueue().addPlayer(((Player) commandSender));
            commandSender.sendMessage(getConfig().getMessage("commands.game.joinetQueue"));
            return true;
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            Player player = ((Player) commandSender);
            if (Game.isInGame(player)) {
                Game game = Game.getGame(player);
                GameSession session = game.getSession();
                session.getOpposite(player);
                int bluepoints = session.getPoints(Color.BLUE);
                int redpoints = session.getPoints(Color.RED);
                game.win(new GameSession.GameData(session.getOpposite(player), player, bluepoints, redpoints));
                player.sendMessage(getConfig().getMessage("commands.game.forlotspill"));
                return true;
            } else if (Queue.isWaiting(player)) {
                Queue.clear(player);
                player.sendMessage(getConfig().getMessage("command.game.forlotQueue"));
            }else {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            String startline = getConfig().getColouredString("format.game.list.startline", new MapBuilder().add(
                    "%nl%", System.lineSeparator()
            ).getMap());
            String endline = getConfig().getColouredString("format.game.endline", new MapBuilder().add(
                    "%nl%", System.lineSeparator()
            ).getMap());
            commandSender.sendMessage(startline);
               Game.getGames().forEach((integer, game) -> commandSender.sendMessage(getConfig().getColouredString("format.game.list.repeat", new MapBuilder().add(
                       "%id%", integer.toString(),
                       "%status%", game.getStatus().name()
               ).getMap())));
           commandSender.sendMessage(endline);
            /*String repeat = getConfig().getColouredString("format.game.list.repeat", new MapBuilder().add(
                    "%id%", String.valueOf(id),
                    "%status%", status.name()
            ).getMap());*/
        } else if (args[0].equalsIgnoreCase("help")) {
        if (commandSender.hasPermission(String.valueOf(getConfig().get("settings.adminPermission")))) {
            getConfig().getFormat("format.game.help.admin").getKeys(true).forEach(sff->commandSender.sendMessage(getConfig().getColouredString(sff)));
        } else {
            getConfig().getFormat("format.game.help.normal").getKeys(true).forEach(sff -> commandSender.sendMessage(getConfig().getColouredString(sff)));
        }
        } else if (!commandSender.hasPermission(String.valueOf(getConfig().get("settings.adminPermission")))) {
            commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
            return true;
        } else if (args[0].equalsIgnoreCase("wand")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            Player player = ((Player) commandSender);
            if (player.getInventory().firstEmpty() == -1)
                return true;

        }

        return true;
    }

    private static boolean isInt(String text) {
        try {
            Integer.parseInt(text);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
