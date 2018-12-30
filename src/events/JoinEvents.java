package events;

import me.main.Main;
import methods.MapManager;
import methods.PlayerManager;
import methods.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvents implements Listener {

    public JoinEvents(Main main) {
    }

    @EventHandler
    public void onTpMap(PlayerJoinEvent e){
        e.setJoinMessage("");
        Player p = e.getPlayer();
        MapManager.mm.tpPlayers(Main.main.currentMapID, p);

        PlayerManager.pm.setArmor(p);
        PlayerManager.pm.setItems(p);

        Main.main.currentks.put(Bukkit.getPlayer(e.getPlayer().getName()), 0);
        p.setHealth(20);
        p.setFoodLevel(20);

        ScoreboardManager.sm.setScoreboard(p);

        Bukkit.getScheduler().runTaskTimer(Main.main.getMain(), () -> {

            ScoreboardManager.sm.updateScoreboard(p);

        }, 0, 20*5);

    }

    @EventHandler
    public void onHideVanish(PlayerJoinEvent e){
        Player p = e.getPlayer();
        for(Player all : Bukkit.getOnlinePlayers()){
            if(Main.main.vanish.contains(all)){
                p.hidePlayer(all);
            }
        }
    }

}
