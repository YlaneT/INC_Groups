import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public class Groups_Manager implements CommandExecutor {
	private static Groups_Manager INSTANCE;
	private static List<Group>    groups;
	
	public static Groups_Manager getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new Groups_Manager();
		}
		return INSTANCE;
	}
	
	public boolean onCommand ( CommandSender sender, Command command, String label, String[] args ) {
		if ((label.equalsIgnoreCase("group") || label.equalsIgnoreCase("g")) && sender instanceof Player) {
			
			Player playerSender = (Player) sender;
			Player playerReceiver;
			
			// TODO : LIST
			// tous les joueurs groupés peuvent l'utiliser
			
			// TODO : INVITE
			// Seuls les chefs de groupes et les joueurs non groupés peuvent l'utiliser
			// /!\ : Si un joueur envoie plusieurs demandes quand il n'est pas encore groupé
			// /!\ : Si un joueur accepte une demande après avoir envoyer une demande
			// /!\ : Cannot invite self
			// /!\ : Cannot invite someone you already invited
			
			// TODO : KICK
			// Seuls les chefs de groupes peuvent l'utiliser
			
			// TODO : QUIT
			// Tous les joueurs groupés peuvent l'utiliser,
			// si un chef de groupe essaie de l'utiliser, il reçoit une notif pour lui demander s'il est sûr
			// (dissolution)
			
			// COPY PASTE
			/*
			if (dueling(playerSender)) {

			}
			
			//----------		DUEL		----------
			
			if (args.length == 0) {
				playerSender.sendMessage(Util.sysMsg("Utilisez /duel help pour des informations sur les commandes."));
				
				// Si le joueur a une demande de duel en attente, on lui envoie aussi la prochaine demande
				for(Demande_Duel dmd : demandes) {
					if (dmd.contains_receiver(playerSender)) {
						playerSender.sendMessage(Util.sysMsg("Vous avez des demandes en attente, voici la prochaine : "));
						playerSender.sendMessage(Util.sysMsg(dmd.toString()));
						playerSender.sendMessage(Util.sysMsg("Acceptez avec /duel accept ou refusez avec /duel " + "refuse"));
						return true;
					}
				}
			}
			
			
			// S'il y a un nom derrière /duel
			else if (args.length == 1) {
				
				String target = args[0];
				playerReceiver = Bukkit.getPlayer(target);
				
				//----------		HELP		----------
				if (target.equalsIgnoreCase("help")) {
					help(playerSender);
				}
				
				//----------		LIST		----------
				else if (target.equalsIgnoreCase("list")) {
					duel_list(playerSender, false);
					return true;
				}
			}*/
		
		}
		return false;
	}
}
