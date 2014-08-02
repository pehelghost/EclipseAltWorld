package be.goossenspi.HospitalRespawn;

import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import be.goossenspi.PlayerStats.PSPlugin;
import be.goossenspi.PlayerStats.PlayerStats;

public class HRPlugin extends JavaPlugin{
	
	private PlayerStats playerstats;
	
	public PlayerStats getPlayerStats(){
		return playerstats;
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public void onEnable(){ //PlayerStats ne charge qu'après.
		
		Plugin[] plugins = getServer().getPluginManager().getPlugins();
		
		for(int i = 0; i < plugins.length; i++){
			Plugin p = plugins[i];
			getLogger().info(p.getName() + " PlayerStats");
			getLogger().info(p.getName().contains("PlayerStats") + "");
			if(p.getName().contains("PlayerStats")){
				playerstats = ((PSPlugin) p).getPlayerStats();
			}
		}
		
		if(playerstats == null){
			getLogger().warning("Impossible to find PlayerStats plugin.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		try {
			if(!playerstats.doesArgExist("HospitalID")){
				playerstats.addArgument("HospitalID", "1", "INT", 10);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new HRListener(this), this);
	}
	
}
