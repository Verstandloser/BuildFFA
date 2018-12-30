package commands;

import me.main.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_setup implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){

            Player p = (Player) cs;
            if(p.hasPermission("buildffa.setup")){

                if(Main.main.setup.contains(p)){

                    Main.main.setup.remove(p);
                    p.sendMessage(Main.main.getPrefix()+"§7Du bist nun nicht mehr im Setup-Modus!");
                    p.setGameMode(GameMode.SURVIVAL);

                }else{

                    Main.main.setup.add(p);
                    p.sendMessage(Main.main.getPrefix()+"§7Du bist nun im Setup-Modus!");
                    p.setGameMode(GameMode.CREATIVE);

                }

            }else{
                p.sendMessage(Main.main.getNoPermissionMessage());
            }

        }

        return true;
    }
}
