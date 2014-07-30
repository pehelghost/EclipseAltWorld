package be.goossenspi.PlayerStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PSPlugin extends JavaPlugin {
	
	public String dblocation = "";
	public String username = "";
	public String password = "";
	public String database = "AltWorld";
	
	private Connection connection;
	
	private PlayerStats ps;
	
	public Connection getConnection(){
		return connection;
	}
	
	public PlayerStats getPlayerStats(){
		return ps;
	}
	
	@Override
	public void onDisable(){
		try {
			if(connection != null) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable(){
		FileConfiguration config = this.getConfig();
		if(!config.contains("dblocation")){
			config.set("dblocation", "localhost");
		}
		if(!config.contains("username")){
			config.set("username", "");
		}
		if(!config.contains("password")){
			config.set("password", "");
		}
		if(!config.contains("dblocation")){
			config.set("dblocation", "localhost");
		}
		if(!config.contains("database")){
			config.set("database", "AltWorld");
		}
		
		this.saveConfig();
		
		dblocation = config.getString("dblocation");
		username = config.getString("username");
		password = config.getString("password");
		database = config.getString("database");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + dblocation + "/" + database, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ps = new PlayerStats(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PSListener(this), this);
	}
	
}
