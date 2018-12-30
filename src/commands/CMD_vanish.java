package commands;

import me.main.Main;
import methods.MapManager;
import methods.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_vanish implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){
            Player p = (Player) cs;
            if(p.hasPermission("buildffa.vanish")){

                if(Main.main.vanish.contains(p)){
                    Main.main.vanish.remove(p);
                    p.sendMessage(Main.main.getPrefix()+"§7Du bist nun §cnicht mehr §7im Vanish!");
                    MapManager.mm.tpPlayers(Main.main.currentMapID, p);
                    PlayerManager.pm.setArmor(p);
                    PlayerManager.pm.setItems(p);
                    p.setAllowFlight(false);

                    for(Player all : Bukkit.getOnlinePlayers()){
                        all.showPlayer(p);
                    }
                }else{
                    Main.main.vanish.add(p);
                    p.sendMessage(Main.main.getPrefix()+"§7Du bist nun §aim §7Vanish!");
                    p.getInventory().clear();
                    p.setAllowFlight(true);

                    for(Player all : Bukkit.getOnlinePlayers()){
                        all.hidePlayer(p);
                    }

                }

            }else{
                p.sendMessage(Main.main.getNoPermissionMessage());
            }
        }

        return true;
    }
}
