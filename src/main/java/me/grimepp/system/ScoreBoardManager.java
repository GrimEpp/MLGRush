package me.grimepp.system;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.grimepp.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardManager extends Default{
    private Map<Integer,String> map;
    private Scoreboard scoreboard;
    private Player p;

    public void enable(Player p ) {
        if (Game.isInGame(p)) {

        } else {
            if (!(Boolean) getConfig().get("settings.scoreboard.enabled"));
            ConfigurationSection section = getConfig().getSection("settings.scoreboard.normal.lines");
            AtomicInteger integer = new AtomicInteger(0);
            ColorSession session = new ColorSession();
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("sc", "dummy");
            Map<Integer, String> stringMap = new HashMap<>();
            section.getKeys(false).forEach(s -> {
                String ss = prepare(section.getString(s), p);
                if (ss.length() > 32) {
                    return;
                }
                String next = session.next();
                Team team = scoreboard.registerNewTeam(s);
                int i = integer.incrementAndGet();
                team.addEntry(next);
                if (ss.length() > 16) {
                    team.setPrefix(ss.substring(0, 16));
                    team.setSuffix(ChatColor.RESET + ss.substring(17, ss.length()));
                } else {
                    team.setPrefix(ss);
                }
                objective.getScore(next).setScore(i);
                stringMap.put(i, s);
            });
            this.map = stringMap;
            this.scoreboard = scoreboard;
            this.p = p;
        }
        update();
    }

    private void update() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline())
                    cancel();
                map.forEach((i, s) -> {
                   Team team = scoreboard.getTeam(s);
                   String real = prepare(getConfig().get("settings.scoreboard.lines."+s), p);
                    if (real.length() > 16) {
                        team.setPrefix(real.substring(0, 16));
                        team.setSuffix(ChatColor.RESET + real.substring(17, real.length()));
                    } else {
                        team.setPrefix(real);
                    }
                });
            }
        }.runTaskTimer(MLGRush.getInstance(), 10, 10);
    }

    private String prepare(String s, Player p) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, s));
    }


    public class ColorSession {
        private boolean append;
        private Queue<ChatColor> colors;
        public ColorSession() {
            colors = new PriorityQueue<>();
            prepare();
        }

        public String next() {
            ChatColor poll = colors.poll();
            if (poll == null) {
                prepare();
                append = true;
                poll = colors.poll();
            }
            if (append) {
                return ChatColor.GREEN.toString() + poll;
            }
            return poll + "";
        }

        private void prepare() {
            for (ChatColor chatColor : ChatColor.values()) {
                if (!chatColor.equals(ChatColor.STRIKETHROUGH) && !chatColor.equals(ChatColor.BOLD) && !chatColor.equals(ChatColor.MAGIC) && !chatColor.equals(ChatColor.ITALIC) && !chatColor.equals(ChatColor.UNDERLINE) )
                colors.add(chatColor);
            }
        }
    }
}
