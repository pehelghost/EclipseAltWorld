package be.goossenspi.PlayerStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerStats {
	
	private PSPlugin plugin;
	
	private String tableName = "Users";
	
	public PlayerStats(PSPlugin plugin){
		this.plugin = plugin;
		
		boolean tableExists = false;
		
		Statement statement = null;
		
		try {
			statement = plugin.getConnection().createStatement();
			
			try {
				ResultSet result = statement.executeQuery("SHOW TABLES FROM AltWorld;");
				for(int i = 1; result.next(); i++){
					if(result.getString(i).equalsIgnoreCase(tableName)) tableExists = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(!tableExists){
				if(!statement.execute("CREATE TABLE " + plugin.getDatabase() + "." + tableName + " (id INT(20) NOT NULL AUTO_INCREMENT , pseudo VARCHAR (30) NOT NULL , PRIMARY KEY (id) ) ENGINE = INODB;")){
					plugin.getLogger().warning("Unable to find or create table " + tableName);
					plugin.getServer().getPluginManager().disablePlugin(plugin);
				}else{
					plugin.getLogger().warning("Created table " + tableName);
				}
			}else{
				plugin.getLogger().warning("Connected to table " + tableName);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if(statement != null){
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateArgument(String arg, String pseudo, String newValue){
		try {
			Statement statement = plugin.getConnection().createStatement();
			
			statement.execute("UPDATE "+ tableName +" SET "+ arg +" = '"+ newValue +"' WHERE pseudo = '"+ pseudo +"';");
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addArgument(String arg, String defaultValue, int maxlength){
		try {
			Statement statement = plugin.getConnection().createStatement();
			
			statement.execute("ALTER TABLE " + tableName + " ADD " + arg + " VARCHAR(" + maxlength + ");");
			statement.execute("UPDATE " + tableName + " SET " + arg + " = '" + defaultValue + "' WHERE id > 0;");
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeArgument(String arg){
		try {
			Statement statement = plugin.getConnection().createStatement();
			
			statement.execute("ALTER TABLE " + tableName + " DROP " + arg + ";");
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
