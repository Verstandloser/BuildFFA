package methods;

import me.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    public static ScoreboardManager sm = new ScoreboardManager();

    public void setScoreboard(Player p){

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        Team kills = sb.registerNewTeam("kills");
        Team deaths = sb.registerNewTeam("deaths");

        Objective obj = sb.registerNewObjective("Stats", "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§e§lBuildFFA");



        obj.getScore("§a ").setScore(10);
        obj.getScore("§6Map").setScore(9);
        obj.getScore("§b» §7"+ MapManager.mm.getMapname(Main.main.currentMapID)).setScore(8);
        obj.getScore("§2 ").setScore(7);
        obj.getScore("§6Kills").setScore(6);
        obj.getScore("§c").setScore(5);
        obj.getScore("§9 ").setScore(4);
        obj.getScore("§6Tode").setScore(3);
        obj.getScore("§3").setScore(2);
        obj.getScore("§e ").setScore(1);
        obj.getScore("§7Teaming erlaubt §c(2)").setScore(0);

        kills.addEntry("§c");
        kills.setPrefix("§7"+StatsManager.sm.getKills(p.getName()));

        deaths.addEntry("§3");
        deaths.setPrefix("§7"+StatsManager.sm.getDeaths(p.getName()));

        p.setScoreboard(sb);
    }

    public void updateScoreboard(Player p){

        if(p.getScoreboard() == null){
            setScoreboard(p);
        }

        Scoreboard sb = p.getScoreboard();

        Team kills = sb.getTeam("kills");
        Team deaths = sb.getTeam("deaths");

        kills.setPrefix("§7"+StatsManager.sm.getKills(p.getName()));
        deaths.setPrefix("§7"+StatsManager.sm.getDeaths(p.getName()));
    }

}
