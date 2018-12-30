package commands;

import me.main.Main;
import methods.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CMD_stats implements CommandExecutor {

    public ArrayList<Player> cooldown = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){

            Player p = (Player) cs;
            if(!cooldown.contains(p)){

                if(args.length == 0){

                    StatsManager.sm.sendStats(p, p.getName());
                    cooldown.add(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                        @Override
                        public void run() {
                            cooldown.remove(p);
                        }
                    });

                }else if(args.length == 1){

                    StatsManager.sm.sendStats(p, args[0]);
                    cooldown.add(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
                        @Override
                        public void run() {
                            cooldown.remove(p);
                        }
                    });
                }

            }else{
                p.sendMessage(Main.main.getPrefix()+"§cDu musst noch warten, bis du diesen Befehl erneut ausführen kannst!");
            }

        }

        return true;
    }
}
