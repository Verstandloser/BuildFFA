package commands;

import me.main.Main;
import methods.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CMD_delete implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){

            Player p = (Player) cs;
            if(Main.main.setup.contains(p)){

                if(args.length==1){

                    MapManager.mm.deleteMap(args[0], p);

                }else{
                    p.sendMessage(Main.main.getPrefix()+"§cNutze: §7/delete <MapID>");
                }

            }else{
                p.sendMessage(Main.main.getPrefix()+"§cDu bist derzeit nicht im Setup-Modus!");
            }

        }

        return true;
    }
}
