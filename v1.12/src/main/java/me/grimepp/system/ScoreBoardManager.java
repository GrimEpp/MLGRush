package me.grimepp.system;

import me.clip.placeholderapi.PlaceholderAPI;
import me.grimepp.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardManager extends Default{
    private Map<Integer,String> map;
    private Scoreboard scoreboard;
    private Player p;
    private boolean checking;

    public static void reset(Player p) {
        new ScoreBoardManager().enable(p);
    }
    public void enable(Player p ) {
        checking = Game.isInGame(p);
        if (Game.isInGame(p)) {
            //Bu("yesss");
            if (!(Boolean) getConfig().get("settings.scoreboard.game.enabled")) return;
            ConfigurationSection section = getConfig().getSection("settings.scoreboard.game.lines");
            AtomicInteger integer = new AtomicInteger(0);
            ColorSession session = new ColorSession();
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("sc", "dummy");
            Map<Integer, String> stringMap = new HashMap<>();
            //Bu("yesjghss");
            GameSession session1 = Game.getGame(p).getSession();
            Stats stats = Stats.getStats(p.getUniqueId());
            section.getKeys(false).forEach(s -> {
                String ss = prepare(getConfig().getColouredConfigString("settings.scoreboard.game.lines."+s, new MapBuilder().add(
                        "%game_points_blue%", String.valueOf(session1.getPoints(Color.BLUE)),
                        "%game_points_red%", String.valueOf(session1.getPoints(Color.RED)),
                        "%game_points_own%", String.valueOf(session1.getPoints(session1.getColor(p))),
                        "%game_points_opponent%", String.valueOf(session1.getPoints(session1.getColor(session1.getOpposite(p)))),
                        "%game_opponent%", session1.getOpposite(p).getName(),
                        "%game_player%", p.getName(),
                        "%stats_losses%", String.valueOf(stats.getLosses()),
                        "%stats_wins%", String.valueOf(stats.getWins())
                ).getMap()), p);
                //Bu(ss);
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
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            this.map = stringMap;
            this.scoreboard = scoreboard;
            this.p = p;
            p.setScoreboard(scoreboard);
            //Bu("yessswatyttt");
        } else {
            //Bu("nooos");
            if (!(Boolean) getConfig().get("settings.scoreboard.normal.enabled")) return;
            ConfigurationSection section = getConfig().getSection("settings.scoreboard.normal.lines");
            AtomicInteger integer = new AtomicInteger(0);
            ColorSession session = new ColorSession();
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("sc", "dummy");
            Map<Integer, String> stringMap = new HashMap<>();
            //Bu("nooo546456s");
            Stats stats = Stats.getStats(p.getUniqueId());
            section.getKeys(false).forEach(s -> {
                String ss = prepare(getConfig().getColouredConfigString("settings.scoreboard.normal.lines."+s, new MapBuilder().add(
                        "%stats_losses%", String.valueOf(stats.getLosses()),
                        "%stats_wins%", String.valueOf(stats.getWins())
                ).getMap()), p);
                //Bu(ss);
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
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            this.map = stringMap;
            this.scoreboard = scoreboard;
            this.p = p;
            p.setScoreboard(scoreboard);
            //Bu("nooos222222222");
        }
        update();
    }

    private void update() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline())
                    cancel();
                if (Game.isInGame(p) != checking)
                    cancel();
               try {
                   if (Game.isInGame(p)) {
                       GameSession session = Game.getGame(p).getSession();
                       map.forEach((i, s) -> {
                           Team team = scoreboard.getTeam(s);
                           String real = prepare(getConfig().getColouredConfigString("settings.scoreboard.game.lines."+s, new MapBuilder().add(
                                   "%game_points_blue%", String.valueOf(session.getPoints(Color.BLUE)),
                                   "%game_points_red%", String.valueOf(session.getPoints(Color.RED)),
                                   "%game_points_own%", String.valueOf(session.getPoints(session.getColor(p))),
                                   "%game_points_opponent%", String.valueOf(session.getPoints(session.getColor(session.getOpposite(p)))),
                                   "%game_opponent%", session.getOpposite(p).getName(),
                                   "%game_player%", p.getName(),
                                   "%stats_losses%", String.valueOf(Stats.getStats(p.getUniqueId()).getLosses()),
                                   "%stats_wins%", String.valueOf(Stats.getStats(p.getUniqueId()).getWins())
                           ).getMap()), p);
                           //Bu(real);
                           if (real.length() > 16) {
                               team.setPrefix(real.substring(0, 16));
                               team.setSuffix(ChatColor.RESET + real.substring(17, real.length()));
                           } else {
                               team.setPrefix(real);
                           }
                       });
                   } else {
                       map.forEach((i, s) -> {
                           Team team = scoreboard.getTeam(s);
                           String real = prepare(getConfig().getColouredConfigString("settings.scoreboard.normal.lines."+s,new MapBuilder().add(
                                   "%stats_losses%", String.valueOf(Stats.getStats(p.getUniqueId()).getLosses()),
                                   "%stats_wins%", String.valueOf(Stats.getStats(p.getUniqueId()).getWins()),
                                   "%stats_losses%", String.valueOf(Stats.getStats(p.getUniqueId()).getLosses()),
                                   "%stats_wins%", String.valueOf(Stats.getStats(p.getUniqueId()).getWins()
                                   )).getMap()), p);
                           if (real.length() > 16) {
                               team.setPrefix(real.substring(0, 16));
                               team.setSuffix(ChatColor.RESET + real.substring(17, real.length()));
                           } else {
                               team.setPrefix(real);
                           }
                       });
                   }
               } catch (NullPointerException e) {
                   cancel();
               }
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
