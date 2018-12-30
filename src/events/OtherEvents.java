package events;

import me.main.Main;
import methods.MapManager;
import methods.MySQL;
import methods.PlayerManager;
import methods.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OtherEvents implements Listener {

    public OtherEvents(Main main) {
    }

    @EventHandler
    public void onInvOverview(InventoryClickEvent e){

        if(e.getInventory().getName().equalsIgnoreCase("§6Dein aktuelles Inventar")){
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){

        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(p.getLocation().getY() < Main.main.currentMapKill){
                if(e.getDamager() instanceof Player){
                    Player t = (Player) e.getDamager();
                    if(!Main.main.vanish.contains(t)){

                        if(Main.main.hitHistory.containsKey(p)){
                            Main.main.hitHistory.remove(p);
                            Main.main.hitHistory.put(p, t);

                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                                @Override
                                public void run() {
                                    Main.main.hitHistory.remove(p);
                                }
                            }, 100);
                        }else{
                            Main.main.hitHistory.put(p, t);

                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                                @Override
                                public void run() {
                                    Main.main.hitHistory.remove(p);
                                }
                            }, 100);
                        }

                    }else{
                        e.setCancelled(true);
                        t.sendMessage(Main.main.getPrefix()+"§7Du darfst im Vanish §ckeine §7Spieler schlagen!");
                    }

                }else{
                    e.setDamage(0);
                }
            }else{
                e.setCancelled(true);
                e.getDamager().sendMessage(Main.main.getPrefix()+"§7Du kannst am Spawn §ckeine §7Spieler schlagen!");
            }
        }

    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        e.setDeathMessage("");
        e.setDroppedExp(0);
        e.setKeepInventory(false);
        e.getEntity().getInventory().clear();
        e.getEntity().spigot().respawn();

        int currentKs = Main.main.currentks.get(Main.main.hitHistory.get(e.getEntity()));
        Main.main.currentks.remove(Main.main.hitHistory.get(e.getEntity()));
        Main.main.currentks.put(Main.main.hitHistory.get(e.getEntity()), currentKs+1);
        PlayerManager.pm.checkKS(Main.main.hitHistory.get(e.getEntity()));

        PlayerManager.pm.checkKSonDeath(e.getEntity());

        StatsManager.sm.addDeath(e.getEntity());
        StatsManager.sm.addKill(Main.main.hitHistory.get(e.getEntity()));

        PlayerManager.pm.broadcastKill(e.getEntity(), Main.main.hitHistory.get(e.getEntity()));

        if(Main.main.currentks.containsKey(e.getEntity())){

            StatsManager.sm.testForNewMaxKs(e.getEntity(), Main.main.currentks.get(e.getEntity()));
            Main.main.currentks.remove(e.getEntity());
            Main.main.currentks.put(e.getEntity(), 0);

        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                MapManager.mm.tpPlayers(Main.main.currentMapID, e.getPlayer());
                PlayerManager.pm.setArmor(e.getPlayer());
                PlayerManager.pm.setItems(e.getPlayer());
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNoFood(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!Main.main.setup.contains(e.getPlayer())){

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                @Override
                public void run() {
                    if(e.getBlock().getType() != Material.AIR){
                        e.getBlock().setType(Material.REDSTONE_BLOCK);
                    }
                }
            }, 20*3);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                @Override
                public void run() {
                    e.getBlock().setType(Material.AIR);
                }
            }, 20*4);

        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!Main.main.setup.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNoSpawnBuild(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(!Main.main.setup.contains(p)){

            if(p.getLocation().getY() >= Main.main.currentMapKill){
                e.setCancelled(true);
                p.sendMessage(Main.main.getPrefix()+"§cDu befindest dich in der Spawn Region! Du kannst hier nicht bauen!");
            }

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        Player p = e.getPlayer();
        if(Main.main.hitHistory.containsKey(p)){

            Bukkit.broadcastMessage(Main.main.getPrefix()+"§cDer Spieler "+p.getName()+" hat sich im Kampf ausgeloggt und hat deshalb §42 §cTode dazu bekommen!");

            Player t = Main.main.hitHistory.get(p);
            StatsManager.sm.addKill(t);
            StatsManager.sm.addDeath(p);
            StatsManager.sm.addDeath(p);
        }

    }

}
