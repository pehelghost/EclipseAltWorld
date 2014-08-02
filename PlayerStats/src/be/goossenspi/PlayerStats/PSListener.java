package be.goossenspi.PlayerStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PSListener implements Listener {
	
	private PSPlugin plugin;
	
	public PSListener(PSPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e){
		try {
			Statement statement = plugin.getConnection().createStatement();
			
			ResultSet result = statement.executeQuery("SELECT * FROM " + plugin.getPlayerStats().getTableName() + " WHERE pseudo = '" + e.getPlayer().getName() + "';");
			
			if(!result.next()){
				int nbCol = result.getMetaData().getColumnCount();
				String query = "INSERT INTO " + plugin.getPlayerStats().getTableName() + " (";
				
				query += result.getMetaData().getColumnLabel(2);
				for(int i = 3; i <= nbCol; i++){
					query += ", " + result.getMetaData().getColumnLabel(i);
				}
				query += ") VALUES ('" + e.getPlayer().getName() + "'";
				for(int i = 3; i <= nbCol; i++){
					Statement tempStatement = plugin.getConnection().createStatement();
					ResultSet tempRes = tempStatement.executeQuery("SELECT " + result.getMetaData().getColumnLabel(i) + " FROM " + plugin.getPlayerStats().getTableName() + " WHERE pseudo ='default';");
					tempRes.next();
					query += ", '" + tempRes.getString(1) + "'";
					tempRes.close();
					tempStatement.close();
				}
				
				query += ");";
				
				statement.execute(query);
			}
			result.close();
			
			statement.close();
		} catch (SQLException e1) {
			e.getPlayer().sendMessage(ChatColor.RED + "Une erreur est survenue lors de votre connexion. Veuillez contacter un administrateur.");
			e1.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
		String[] params = e.getMessage().split(" ");
		
		if(params[0].equalsIgnoreCase("/ps") || params[0].equalsIgnoreCase("/playerstats")){
			e.setCancelled(true);
			
			if(!e.getPlayer().isOp()){
				e.getPlayer().sendMessage(ChatColor.RED + "You don't have access to this command.");
				return;
			}
			
			if(params.length < 2){
				e.getPlayer().sendMessage(ChatColor.RED + "/ps <add|remove|update|sql>");
				return;
			}
			
			if(params[1].equalsIgnoreCase("sql")){
				try {
					Statement statement = plugin.getConnection().createStatement();
					if(statement.execute(e.getMessage().replaceFirst(params[0], "").replaceFirst(params[1], ""))){
						e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully executed the query.");
					}else{
						e.getPlayer().sendMessage(ChatColor.RED + "Unable to execute the query.");
					}
				} catch (SQLException e1) {
					e.getPlayer().sendMessage(ChatColor.RED + "Unable to execute the query.");
					e1.printStackTrace();
				}
			}else if(params[1].equalsIgnoreCase("add")){
				if(params.length < 5){
					e.getPlayer().sendMessage(ChatColor.RED + "/ps add <arg name> <default value> <max length>");
					return;
				}
				
				try{
					plugin.getPlayerStats().addArgument(params[2], params[3], Integer.parseInt(params[4]));
					e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully added " + params[2] + " argument with default value " + params[3] + " and max length " + params[4] + ".");
				}catch(NumberFormatException exc){
					e.getPlayer().sendMessage(ChatColor.RED + "/ps add <arg name> <default value> <max length>");
				} catch (SQLException e1) {
					e.getPlayer().sendMessage(ChatColor.RED + "An error occurred during the execution of the query.");
				}
			}else if(params[1].equalsIgnoreCase("remove")){
				if(params.length < 3){
					e.getPlayer().sendMessage(ChatColor.RED + "/ps remove <arg name>");
					return;
				}
				
				try {
					plugin.getPlayerStats().removeArgument(params[2]);
					e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully removed " + params[2] + " argument.");
				} catch (SQLException e1) {
					e.getPlayer().sendMessage(ChatColor.RED + "An error occurred during the execution of the query.");
				}
			}else if(params[1].equalsIgnoreCase("update")){
				if(params.length < 5){
					e.getPlayer().sendMessage(ChatColor.RED + "/ps update <arg name> <pseudo> <new value>");
					return;
				}
				
				try {
					plugin.getPlayerStats().updateArgument(params[2], params[3], params[4]);
					e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully updated " + params[2] + " argument for player " + params[3] + " to " + params[4] + ".");
				} catch (SQLException e1) {
					e.getPlayer().sendMessage(ChatColor.RED + "An error occurred during the execution of the query.");
				}
			}
		}
	}
}
