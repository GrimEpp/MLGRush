package me.grimepp.system;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;


public class GameSession extends Default {
    private Player team1, team2;
    private int points1, points2;
    GameSession(Player... players) {
        team1 = players[ThreadLocalRandom.current().nextInt(0, 1)];
        team2 = players[0] == team1 ? players[1] : players[0];
        points1 = 0;
        points2 = 0;
    }


    public void win(Color color) {
        if (color == Color.BLUE) {
            points1++;
        } else {
            points2++;
        }
    }

}
