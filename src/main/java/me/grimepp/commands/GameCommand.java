package me.grimepp.commands;

import me.grimepp.system.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            if (isNotInt(game)) {
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
            if (Queue.isWaiting((Player) commandSender)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.erAlleredeIEnVenteKOO"));
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
                player.sendMessage(getConfig().getMessage("commands.game.forlotQueue"));
            }else {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage(getConfig().getColouredString("format.game.list.startline"));
            Game.getGames().forEach((integer, game) -> commandSender.sendMessage(getConfig().getColouredString("format.game.list.repeat", new MapBuilder().add(
                    "%id%", integer.toString(),
                    "%status%", game.getStatus().name()
            ).getMap())));
            commandSender.sendMessage(getConfig().getColouredString("format.game.list.endline"));
        } else if (args[0].equalsIgnoreCase("help")) {
        if (commandSender.hasPermission((String) getConfig().get("settings.adminPermission"))) {
            getConfig().getFormat("format.game.help.admin").getKeys(false).forEach(sff->commandSender.sendMessage(getConfig().getColouredString("format.game.help.admin."+sff)));
        } else {
            getConfig().getFormat("format.game.help.normal").getKeys(false).forEach(sff->commandSender.sendMessage(getConfig().getColouredString("format.game.help.normal."+sff)));
        }
        } else if (!commandSender.hasPermission((String) getConfig().get("settings.adminPermission"))) {
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
            player.getInventory().addItem(Wand.getWand());
        } else if (args[0].equalsIgnoreCase("creategame")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            Player p = (Player) commandSender;
            Wand wand = Wand.get(p);
            if (!wand.isValid()) {
                p.sendMessage(getConfig().getMessage("commands.game.ikkeSattPoints"));
                return true;
            }
            Game game = Game.createGame(new Cube(wand.getL1(), wand.getL2()), wand.getSpawn1(), wand.getBed1(), wand.getSpawn2(), wand.getBed2());
            p.sendMessage(getConfig().getMessage("commands.game.lagdespill", new MapBuilder().add(
                    "%id%", String.valueOf(game.getId())
            ).getMap()));
        } else if (args[0].equalsIgnoreCase("spawn") && args.length > 1) {
            if (isNotInt(args[1])) {
                 commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            int i = Integer.parseInt(args[1]);
            if (i != 1 && i != 2) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            Player p = (Player) commandSender;
            Wand wand = Wand.get(p);
            if (i == 1) {
                wand.setSpawn1(p.getLocation());
                p.sendMessage(getConfig().getMessage("commands.game.sattspawn1"));
                return true;
            } else {
                wand.setSpawn2(p.getLocation());
                p.sendMessage(getConfig().getMessage("commands.game.sattspawn2"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (isNotInt(args[1])) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
                return true;
            }
            int i = Integer.parseInt(args[1]);
            if (Game.getGame(i) == null) {
                commandSender.sendMessage(getConfig().getMessage("commands.game.ingenspillmedid", new MapBuilder().add(
                        "%id%", String.valueOf(i)
                ).getMap()));
                return true;
            }
            Game.deleteGame(i);
        } else {
            commandSender.sendMessage(getConfig().getMessage("commands.game.feilbruk"));
            return true;
        }
        return true;
    }

    private static boolean isNotInt(String text) {
        try {
            Integer.parseInt(text);
            return false;

        } catch (NumberFormatException e) {
            return true;
        }
    }
}
