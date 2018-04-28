package me.grimepp.events;

import me.grimepp.system.Game;
import me.grimepp.system.GameSession;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Game.isInGame(e.getPlayer())) {
            Player player = e.getPlayer();
            Game game = Game.getGame(player);
            GameSession session = game.getSession();
            session.getOpposite(player);
            int bluepoints = session.getPoints(Color.BLUE);
            int redpoints = session.getPoints(Color.RED);
            game.win(new GameSession.GameData(session.getOpposite(player), player, bluepoints, redpoints));

        }
    }
}
