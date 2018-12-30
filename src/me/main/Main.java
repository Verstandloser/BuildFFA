package me.main;

import commands.*;
import events.JoinEvents;
import events.LoginEvents;
import events.MoveEvents;
import events.OtherEvents;
import methods.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main main;

    public HashMap<Player, Player> hitHistory = new HashMap<>();
    public HashMap<Player, Integer> currentks = new HashMap<>();

    public ArrayList<Player> setup = new ArrayList<>();
    public ArrayList<Player> vanish = new ArrayList<>();

    public int currentMapID;
    public int currentMapDeath;
    public int currentMapKill;

    @Override
    public void onEnable() {

        main = this;

        createFile();
        MySQL.sql.connect();

        registerCommands();
        registerEvents();

        currentMapID = 1;
        currentMapDeath = MapManager.mm.getDeathHigh(currentMapID);
        currentMapKill = MapManager.mm.getKillHigh(currentMapID);
        PlayerManager.pm.startCounter();

        MapManager.mm.changeMap();
        MapManager.mm.broadcastNewMap();

        PlayerManager.pm.registerItems();
        PlayerManager.pm.startCounter();
    }

    @Override
    public void onDisable() {

        for(Player all : Bukkit.getOnlinePlayers()){
            Main.main.hitHistory.remove(all);
            all.kickPlayer("§cDer BuildFFA Server wurde gestoppt! Bitte versuche es später erneut!");
        }

        MySQL.sql.close();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new MoveEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new LoginEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new OtherEvents(this), this);

    }

    private void registerCommands() {
        getCommand("setup").setExecutor(new CMD_setup());
        getCommand("addmap").setExecutor(new CMD_addmap());
        getCommand("delete").setExecutor(new CMD_delete());
        getCommand("setitems").setExecutor(new CMD_setitems());
        getCommand("vanish").setExecutor(new CMD_vanish());
        getCommand("stats").setExecutor(new CMD_stats());
    }

    private void createFile(){
        File file = new File("plugins/BuildFFA", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            cfg.set("host", "localhost");
            cfg.set("database", "Datenbank");
            cfg.set("user", "root");
            cfg.set("password", "PASSWORT");
            cfg.set("port", 3306);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Main getMain(){
        return main;
    }

    public String getPrefix(){
        return "§eBuildFFA §f● §7";
    }

    public String getNoPermissionMessage(){
        return getPrefix()+"§cDu hast keine Rechte!";
    }
}
