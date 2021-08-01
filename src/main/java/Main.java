import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	
	
	@Override
	public void onEnable () {
		PluginManager pm = getServer().getPluginManager();
		
		getLogger().info("[Groups] has been enable");
		getCommand("group").setExecutor(Groups_Manager.getInstance());
	}
	// TODO : Créer une boucle qui efface les invitations expirées toutes les x minutes
	// pour éviter le stockage de données inutiles
	
	@Override
	public void onDisable () {
		getLogger().info("[Groups] has been disabled");
	}
}
