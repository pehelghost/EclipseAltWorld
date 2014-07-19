package be.goossenspi.PlayerStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PSPlugin extends JavaPlugin {
	
	public String dblocation = "mysql1.alwaysdata.com";
	public String username = "93761";
	public String password = "goossens";
	public String database = "pehelghost_tests";
	
	public void test(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + dblocation + "/" + database, username, password);
			
			Statement statement = connection.createStatement();
			
			//NICKEL
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PSListener(this), this);
	}
	
}
