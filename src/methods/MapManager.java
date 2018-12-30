package methods;

import me.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapManager {

    public static MapManager mm = new MapManager();

    public void addMap(String mapid, String mapname, String deathhigh, String killhigh, Player sender){
        if(!existsName(mapname)){
            if(!existsID(mapid)){
                if(isInt(deathhigh)){
                    if(isInt(killhigh)){
                        if(isInt(mapid)){

                            Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
                                @Override
                                public void run() {
                                    Location loc = sender.getLocation();
                                    MySQL.sql.update("INSERT INTO buildffa_maps (mapid, mapname, deathhigh, killhigh, worldname, " +
                                            "x, y, z, yaw, pitch) VALUES ('"+mapid+"','"+mapname+"','"+deathhigh+"','"+killhigh+
                                            "','"+loc.getWorld().getName()+"','"+loc.getX()+"','"+loc.getY()+"','"+loc.getZ()+
                                            "','"+loc.getYaw()+"','"+loc.getPitch()+"')");

                                    sender.sendMessage(Main.main.getPrefix()+"§7Die Map §a"+mapname+" §7wurde erstellt!");
                                }
                            });

                        }else{
                            sender.sendMessage(Main.main.getPrefix()+"§cDie 'mapid' muss eine Zahl sein!");
                        }
                    }else{
                        sender.sendMessage(Main.main.getPrefix()+"§cDie 'killhigh' muss eine Zahl sein!");
                    }
                }else{
                    sender.sendMessage(Main.main.getPrefix()+"§cDie 'deathhigh' muss eine Zahl sein!");
                }
            }else{
                sender.sendMessage(Main.main.getPrefix()+"§cDiese Map-ID wird bereits verwendet!");
            }
        }else{
            sender.sendMessage(Main.main.getPrefix()+"§cDieser Mapname wird bereits verwendet!");
        }
    }

    public void deleteMap(String mapid, Player sender){

        if(isInt(mapid)){

            if(existsID(mapid)){

                Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.main.getMain(), new Runnable() {
                    @Override
                    public void run() {

                        MySQL.sql.update("DELETE FROM buildffa_maps WHERE mapid='"+mapid+"'");
                        sender.sendMessage(Main.main.getPrefix()+"§7Du hast die Map §centfernt§7!");

                    }
                });

            }else{
                sender.sendMessage(Main.main.getPrefix()+"§cDiese MapID gibt es nicht!");
            }

        }else{
            sender.sendMessage(Main.main.getPrefix()+"§cDie 'mapid' muss eine Zahl sein!");
        }

    }

    public boolean existsName(String name){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapname='"+name+"'");
        try {
            while (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean existsID(String id){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapid='"+id+"'");
        try {
            while (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isInt(String number){
        try {
            int i = Integer.parseInt(number);
            return true;
        }catch (NumberFormatException nfm){
            nfm.printStackTrace();
        }
        return false;
    }

    public String getMapname(int mapid){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapid='"+mapid+"'");
        try {
            while (rs.next()){
                return rs.getString("mapname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    public int getKillHigh(int mapid){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapid='"+mapid+"'");
        try {
            while (rs.next()){
                return rs.getInt("killhigh");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getDeathHigh(int mapid){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapid='"+mapid+"'");
        try {
            while (rs.next()){
                return rs.getInt("deathhigh");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void tpPlayers(Integer mapid, Player player){

        ResultSet rs = MySQL.sql.query("SELECT * FROM buildffa_maps WHERE mapid='"+mapid+"'");
        try {
            while (rs.next()){
                String world = rs.getString("worldname");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float yaw = (float) rs.getDouble("yaw");
                float pitch = (float) rs.getDouble("pitch");

                Location loc = player.getLocation();
                loc.setWorld(Bukkit.getWorld(world));
                loc.setX(x);
                loc.setY(y);
                loc.setZ(z);
                loc.setYaw(yaw);
                loc.setPitch(pitch);

                player.teleport(loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void changeMap(){

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {

                if(existsID(String.valueOf(Main.main.currentMapID+1))){

                    Main.main.currentMapID = Main.main.currentMapID+1;
                    Bukkit.broadcastMessage("§a ");
                    Bukkit.broadcastMessage(Main.main.getPrefix()+"§7§lDie Map §a§l"+getMapname(Main.main.currentMapID)+ " §7§lwird nun gespielt!");
                    Bukkit.broadcastMessage("§a ");
                    changeMap();
                    broadcastNewMap();

                    for(Player all : Bukkit.getOnlinePlayers()){
                        tpPlayers(Main.main.currentMapID, all);
                    }

                }else{
                    if(Main.main.currentMapID != 1){
                        Main.main.currentMapID=1;
                        Bukkit.broadcastMessage("§a ");
                        Bukkit.broadcastMessage(Main.main.getPrefix()+"§7§lDie Map §a§l"+getMapname(Main.main.currentMapID)+ " §7§lwird nun gespielt!");
                        Bukkit.broadcastMessage("§a ");
                        changeMap();
                        broadcastNewMap();

                        for(Player all : Bukkit.getOnlinePlayers()){
                            tpPlayers(Main.main.currentMapID, all);
                            all.playSound(all.getLocation(), Sound.LEVEL_UP, 3, 2);
                        }
                    }else{
                        changeMap();
                        broadcastNewMap();
                    }

                }

            }
        }, 600000);

    }

    public void broadcastNewMap(){

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main.getMain(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(Main.main.getPrefix()+"§7§lDie Map wecheslt in §4§l10 §7§lSekunden!");
            }
        }, 599900);

    }



}
