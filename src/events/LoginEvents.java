package events;

import me.main.Main;
import methods.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginEvents implements Listener {

    public LoginEvents(Main main) {
    }

    @EventHandler
    public void onregisterInTable(PlayerPreLoginEvent e){
        if(!isInTable(e.getName())){
            MySQL.sql.update("INSERT INTO buildffa_players (playername, kills, deaths, max_ks, sword, " +
                    "rod, bow, arrow, blocks) VALUES ('"+e.getName()+"','"+0+"','"+0+"','"+0+"','"+0+"','"+1+"','"+
                    2+"','"+4+"','"+8+"')");
        }
    }

    private boolean isInTable(String name){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+name+"'");
        try {
            while (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
