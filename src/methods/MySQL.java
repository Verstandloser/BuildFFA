package methods;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;

public class MySQL {
    public static MySQL sql = new MySQL();

    public String host, database, user, password;
    public Integer port, currentcon;
    public Connection c1, c2;


    public void connect() {
        try {

            File file = new File("plugins/BuildFFA", "config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            this.host = cfg.getString("host");
            this.database = cfg.getString("database");
            this.user = cfg.getString("user");
            this.password = cfg.getString("password");
            this.port = cfg.getInt("port");


            c1 = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, password);
            c2 = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, password);

            currentcon = 1;

            Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §aVerbunden");
            createTables();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        switch (currentcon) {
            case 1:
                currentcon = 2;
                return c1;
            case 2:
                currentcon = 1;
                return c2;
        }
        return null;
    }

    public void createTables(){
        try {
            getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS buildffa_maps (mapid INTEGER(100), " +
                    "mapname VARCHAR(100), deathhigh INTEGER(100), killhigh INTEGER(100), worldname VARCHAR(100), " +
                    "x INTEGER(100), y INTEGER(100), z INTEGER(100), yaw INTEGER(100), pitch INTEGER(100))");

            getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS buildffa_players (playername VARCHAR(100), " +
                    "kills INTEGER(100), deaths INTEGER(100), max_ks INTEGER(100), " +
                    "sword INTEGER(100), rod INTEGER(100), bow INTEGER(100), arrow INTEGER(100), blocks INTEGER(100))");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void close(){
        try {
            c1.close();
            c2.close();
            Bukkit.getConsoleSender().sendMessage("§f[§9MySQL§f] §cVerbindung geschlossen");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§f[§9MySQL§f] §cFehler beim schließen der Verbindung!");
        }
    }

    public void update(String qry) {
        try {
            Statement st = getConnection().createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            return getConnection().createStatement().executeQuery(qry);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
