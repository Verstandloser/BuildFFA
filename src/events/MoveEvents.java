package events;

import me.main.Main;
import methods.MapManager;
import methods.PlayerManager;
import methods.StatsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MoveEvents implements Listener {

    public MoveEvents(Main main) {
    }

    @EventHandler
    public void onDeath(PlayerMoveEvent e){

        Player p = e.getPlayer();

        if(!Main.main.vanish.contains(p)){
            if(p.getLocation().getY() < Main.main.currentMapDeath){

                e.setCancelled(true);

                if(Main.main.hitHistory.containsKey(p)){

                    StatsManager.sm.addDeath(p);
                    StatsManager.sm.addKill(Main.main.hitHistory.get(p));

                    Player t = Main.main.hitHistory.get(p);
                    int currentKs = Main.main.currentks.get(t);
                    Main.main.currentks.remove(t);
                    Main.main.currentks.put(t, currentKs+1);
                    PlayerManager.pm.checkKS(t);

                    PlayerManager.pm.checkKSonDeath(p);

                    PlayerManager.pm.broadcastKill(p, t);

                    PlayerManager.pm.setArmor(p);
                    PlayerManager.pm.setItems(p);

                    MapManager.mm.tpPlayers(Main.main.currentMapID, p);
                    p.setHealth(20);

                    if(Main.main.currentks.containsKey(p)){

                        StatsManager.sm.testForNewMaxKs(p, Main.main.currentks.get(p));
                        Main.main.currentks.remove(p);
                        Main.main.currentks.put(p, 0);

                    }

                }else{
                    StatsManager.sm.addDeath(p);

                    PlayerManager.pm.setArmor(p);
                    PlayerManager.pm.setItems(p);

                    PlayerManager.pm.checkKSonDeath(p);

                    MapManager.mm.tpPlayers(Main.main.currentMapID, p);
                    p.setHealth(20);

                    if(Main.main.currentks.containsKey(p)){

                        StatsManager.sm.testForNewMaxKs(p, Main.main.currentks.get(p));
                        Main.main.currentks.remove(p);
                        Main.main.currentks.put(p, 0);

                    }

                }


            }

        }

    }

}
