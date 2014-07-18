package be.goossenspi.PlayerStats;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PSPlugin extends JavaPlugin {
	
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PSListener(this), this);
		//test2
	}
	
}
