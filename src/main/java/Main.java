import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable (){
		PluginManager pm       = getServer().getPluginManager();
		
		getLogger().info("[Groups] has been enable");
		getCommand("g").setExecutor(Groups_Manager.getInstance());
	}
}
