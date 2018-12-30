package methods;

import me.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsManager {

    public static StatsManager sm = new StatsManager();

    public void addKill(Player p){


        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+p.getName()+"'");

                try {
                    while (rs.next()){

                        int currentKills = rs.getInt("kills");
                        currentKills = currentKills+1;
                        MySQL.sql.update("UPDATE buildffa_players SET kills = "+currentKills+" WHERE playername ='"+p.getName()+"'");

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public void addDeath(Player p){

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+p.getName()+"'");

                try {
                    while (rs.next()){

                        int currentDeaths = rs.getInt("deaths");
                        currentDeaths = currentDeaths+1;
                        MySQL.sql.update("UPDATE buildffa_players SET deaths = "+currentDeaths+" WHERE playername ='"+p.getName()+"'");

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public void testForNewMaxKs(Player p, int ks){

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {

                ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players");

                try {
                    while (rs.next()){

                        int maxks = rs.getInt("max_ks");
                        if(maxks < ks){
                            MySQL.sql.update("UPDATE buildffa_players SET max_ks = "+ks+" WHERE playername ='"+p.getName()+"'");
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void sendStats(Player receiver, String name){

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+name+"'");

                try {
                    while (rs.next()){
                        receiver.sendMessage("§3§m-----------------------");
                        receiver.sendMessage("§aKills: §f"+rs.getInt("kills"));
                        receiver.sendMessage("§aDeaths: §f"+rs.getInt("deaths"));
                        receiver.sendMessage("§aK/D: §f"+rs.getInt("kills")/rs.getInt("deaths"));
                        receiver.sendMessage("§aMax. Killstreak: §c"+rs.getInt("max_ks"));
                        receiver.sendMessage("§3§m-----------------------");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public int getKills(String name){
        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+name+"'");

        try {
            while (rs.next()){
                return rs.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getDeaths(String name){
        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+name+"'");

        try {
            while (rs.next()){
                return rs.getInt("deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
