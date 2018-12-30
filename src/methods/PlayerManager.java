package methods;

import me.main.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PlayerManager {

    public static PlayerManager pm = new PlayerManager();

    public void startCounter(){

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                setCurrentKS();
                startCounter();
            }
        }, 100);
    }

    public void setCurrentKS(){
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers()){
                    if(Main.main.currentks.containsKey(all)){
                        all.setLevel(Main.main.currentks.get(all));
                    }
                }
            }
        });
    }

    public void setArmor(Player p){

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta hm = helmet.getItemMeta();
        hm.setDisplayName("§a» §7Helm");
        hm.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        helmet.setItemMeta(hm);

        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta cm = chest.getItemMeta();
        cm.setDisplayName("§a» §7Brustplatte");
        cm.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        chest.setItemMeta(cm);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta lm = leggings.getItemMeta();
        lm.setDisplayName("§a» §7Hose");
        lm.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        leggings.setItemMeta(lm);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta bom = boots.getItemMeta();
        bom.setDisplayName("§a» §7Helm");
        bom.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        boots.setItemMeta(bom);

        p.getInventory().clear();

        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chest);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);



    }

    public void registerItems(){
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta sm = sword.getItemMeta();
        sm.setDisplayName("§a» §7Schwert");
        sm.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        sword.setItemMeta(sm);

        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta rm = rod.getItemMeta();
        rm.setDisplayName("§a» §7Angel");
        rod.setItemMeta(rm);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bm = bow.getItemMeta();
        bm.setDisplayName("§a» §7Bogen");
        bm.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        bow.setItemMeta(bm);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta am = arrow.getItemMeta();
        am.setDisplayName("§a» §7Pfeile");
        arrow.setItemMeta(am);
        arrow.setAmount(3);

        ItemStack blocks = new ItemStack(Material.SANDSTONE);
        ItemMeta blm = blocks.getItemMeta();
        blm.setDisplayName("§a» §7Blöcke");
        blocks.setAmount(32);
        blocks.setItemMeta(blm);

        findItemstack.put("Schwert", sword);
        findItemstack.put("Angel", rod);
        findItemstack.put("Bogen", bow);
        findItemstack.put("Pfeil", arrow);
        findItemstack.put("Block", blocks);

        findItemmeta.put("Schwert", sm);
        findItemmeta.put("Angel", rm);
        findItemmeta.put("Bogen", bm);
        findItemmeta.put("Pfeil", am);
        findItemmeta.put("Block", blm);
    }

    public HashMap<String, ItemStack> findItemstack = new HashMap<>();
    public HashMap<String, ItemMeta> findItemmeta = new HashMap<>();

    public void setItems(Player p){

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {

                ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_players WHERE playername='"+p.getName()+"'");
                try {
                    while (rs.next()){

                        ItemStack sword = findItemstack.get("Schwert");
                        ItemMeta sm = findItemmeta.get("Schwert");
                        sword.setItemMeta(sm);

                        ItemStack rod = findItemstack.get("Angel");
                        ItemMeta rm = findItemmeta.get("Angel");
                        rod.setItemMeta(rm);

                        ItemStack bow = findItemstack.get("Bogen");
                        ItemMeta bm = findItemmeta.get("Bogen");
                        bow.setItemMeta(bm);

                        ItemStack arrow = findItemstack.get("Pfeil");
                        ItemMeta am = findItemmeta.get("Pfeil");
                        arrow.setItemMeta(am);

                        ItemStack blocks = findItemstack.get("Block");
                        ItemMeta blm = findItemmeta.get("Block");
                        blocks.setItemMeta(blm);

                        p.getInventory().setItem(rs.getInt("sword"), sword);
                        p.getInventory().setItem(rs.getInt("rod"), rod);
                        p.getInventory().setItem(rs.getInt("bow"), bow);
                        p.getInventory().setItem(rs.getInt("arrow"), arrow);
                        p.getInventory().setItem(rs.getInt("blocks"), blocks);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void broadcastKill(Player victim, Player killer){
        killer.sendMessage(Main.main.getPrefix()+"§7Du hast §a"+victim.getName()+" §7getötet");
        victim.sendMessage(Main.main.getPrefix()+"§7Du wurdest von §a"+killer.getName()+" §7getötet");

    }

    public void checkKS(Player p){

        if(Main.main.currentks.containsKey(p)){
            int i = Main.main.currentks.get(p);
            if(i == 3){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 6){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 9){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 12){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 15){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 18){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 21){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 24){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 27){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 30){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 33){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 36){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §3"+i+"er §7Killstreak erreicht!");
            }else if(i == 39){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 42){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 45){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 48){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 51){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 54){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 57){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }else if(i == 60){
                Bukkit.broadcastMessage(Main.main.getPrefix()+" §7Der Spieler §a"+p.getName()+" §7hat eine §33er §7Killstreak erreicht!");
            }

        }

    }

    public void checkKSonDeath(Player p){
        if(Main.main.currentks.containsKey(p)){
            int i = Main.main.currentks.get(p);
            if(i >= 3){
                Bukkit.broadcastMessage(Main.main.getPrefix()+"§cDer Spieler §7"+p.getName()+" §cist mit einer §2"+i+"er §cKillstreak gestorben!");
            }
        }
    }

}
