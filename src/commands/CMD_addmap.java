package commands;

import me.main.Main;
import methods.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_addmap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){

            Player p = (Player) cs;
            if(Main.main.setup.contains(p)){

                // /addmap <Mapname> <Todeshöhe> <Killhöhe> <Map-ID>
                if(args.length == 4){

                    MapManager.mm.addMap(args[3], args[0], args[1], args[2], p);

                }else{
                    p.sendMessage("§cNutze: §7/addmap <mapname> <deathhigh> <killhigh> <mapid>\n" +
                            "§aMapname: §7Name, welcher später im Scoreboard sichtbar ist!\n" +
                            "§adeathhigh: §7Ab dieser Höhewerden Spieler automatisch sterben!\n" +
                            "§aKillhigh: §7Ab dieser Höhe erhalten Spieler ihr Inventar und können sich angreifen!\n" +
                            "§aMapID: §7Die ID der Map ist für den Map-Wechsel wichtig!");
                }

            }else{
                p.sendMessage(Main.main.getPrefix()+"§cDu bist derzeit nicht im Setup-Modus!");
            }

        }

        return true;
    }
}
