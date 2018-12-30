package commands;

import me.main.Main;
import methods.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CMD_setitems implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(cs instanceof Player){

            Player p = (Player) cs;
            if(args.length==2){

                if(isAValidNumber(args[1])){

                    if(isNotOnASlot(p.getName(), Integer.parseInt(args[1]))){
                        if(args[0].equalsIgnoreCase("Schwert")){
                            setItem(p.getName(), "sword", Integer.parseInt(args[1]));
                            p.sendMessage(Main.main.getPrefix()+"§7Das Item wurde festgelegt! §c/setitems open");
                        }else if(args[0].equalsIgnoreCase("Angel")){
                            setItem(p.getName(), "rod", Integer.parseInt(args[1]));
                            p.sendMessage(Main.main.getPrefix()+"§7Das Item wurde festgelegt! §c/setitems open");
                        }else if(args[0].equalsIgnoreCase("Bogen")){
                            setItem(p.getName(), "bow", Integer.parseInt(args[1]));
                            p.sendMessage(Main.main.getPrefix()+"§7Das Item wurde festgelegt! §c/setitems open");
                        }else if(args[0].equalsIgnoreCase("Pfeile")){
                            setItem(p.getName(), "arrow", Integer.parseInt(args[1]));
                            p.sendMessage(Main.main.getPrefix()+"§7Das Item wurde festgelegt! §c/setitems open");
                        }else if(args[0].equalsIgnoreCase("Blöcke")) {
                            setItem(p.getName(), "blocks", Integer.parseInt(args[1]));
                            p.sendMessage(Main.main.getPrefix()+"§7Das Item wurde festgelegt! §c/setitems open");
                        }else{
                            p.sendMessage(Main.main.getPrefix()+"§cDieses Item wird nicht unterstützt!");
                        }
                    }else{
                        p.sendMessage(Main.main.getPrefix()+"§cDieser Slot ist bereits belegt!");
                    }

                }else{
                    p.sendMessage(Main.main.getPrefix()+"§cBitte gib eine Zahl zwischen 1 und 9 bei dem Item an!");
                }

            }else if(args.length == 1){

                if(args[0].equalsIgnoreCase("open")){

                    openInv(p);

                }else{
                    p.sendMessage(Main.main.getPrefix()+"§cNutze: §f/setitems <Item> <Slot>\n§eMögliche Items: §3Schwert, Angel, " +
                            "Bogen, Pfeile, Blöcke");
                    p.sendMessage("§3Eine Übersicht deines aktuellen Inventares erhälst du mit §2/setitems open");                }

            }else{
                p.sendMessage(Main.main.getPrefix()+"§cNutze: §f/setitems <Item> <Slot>\n§eMögliche Items: §3Schwert, Angel, " +
                        "Bogen, Pfeile, Blöcke");
                p.sendMessage("§3Eine Übersicht deines aktuellen Inventares erhälst du mit §2/setitems open");

            }

        }

        return true;
    }

    private boolean isAValidNumber(String number){
        try {
            int i = Integer.parseInt(number);
            if(i >= 1 ){
                if(i <= 9){
                    return true;
                }
            }
        }catch (NumberFormatException nfm){
            nfm.printStackTrace();
        }
        return false;
    }

    private void setItem(String name, String item, int slot){

        slot = slot-1;
        MySQL.sql.update("UPDATE buildffa_players SET "+item+" = "+slot+" WHERE playername ='"+name+"'");

    }

    private boolean isNotOnASlot(String name, int slot){
        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+name+"'");

        try {
            while (rs.next()){
                slot = slot-1;
                if(rs.getInt("sword") != slot){
                    if(rs.getInt("rod") != slot){
                        if(rs.getInt("bow") != slot){
                            if(rs.getInt("arrow") != slot){
                                if(rs.getInt("blocks") != slot){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void openInv(Player p){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername ='"+p.getName()+"'");

        try {
            while (rs.next()){

                Inventory inv = Bukkit.createInventory(null, 9, "§6Dein aktuelles Inventar");

                ItemStack sword = new ItemStack(Material.STONE_SWORD);

                ItemStack rod = new ItemStack(Material.FISHING_ROD);

                ItemStack bow = new ItemStack(Material.BOW);

                ItemStack arrows = new ItemStack(Material.ARROW);

                ItemStack blocks = new ItemStack(Material.SANDSTONE);

                inv.setItem(rs.getInt("sword"), sword);
                inv.setItem(rs.getInt("rod"), rod);
                inv.setItem(rs.getInt("bow"), bow);
                inv.setItem(rs.getInt("arrow"), arrows);
                inv.setItem(rs.getInt("blocks"), blocks);

                p.openInventory(inv);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
