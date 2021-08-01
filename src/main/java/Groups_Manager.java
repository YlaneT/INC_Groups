import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class Groups_Manager implements CommandExecutor {
	private static Groups_Manager   INSTANCE;
	private        List<Group>      groups;
	private        List<Invitation> invitations;
	
	public static Groups_Manager getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new Groups_Manager();
			Groups_Manager.getInstance().groups = new ArrayList<Group>();
			Groups_Manager.getInstance().invitations = new ArrayList<Invitation>();
		}
		return INSTANCE;
	}
	
	public static boolean isGrouped ( Player p ) {
		for(Group g : Groups_Manager.getInstance().getGroups()) {
			if (g.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	public static Group whichGroup ( Player p ) {
		for(Group g : Groups_Manager.getInstance().getGroups()) {
			if (g.contains(p)) {
				return g;
			}
		}
		return null;
	}
	
	public static boolean isChief ( Player playerSender ) {
		if (isGrouped(playerSender)) {
			return playerSender.equals(whichGroup(playerSender).getChief());
		}
		return false;
	}
	
	public static Invitation getInvFromHash ( int hash ) {
		for(Invitation invitation : getInstance().getInvitations()) {
			if (invitation.hashCode() == hash) {
				return invitation;
			}
		}
		return null;
	}
	
	private static boolean invitationExists ( int hash ) {
		Invitation inv = getInvFromHash(hash);
		return inv != null;
	}
	
	private static void showHelp ( Player player ) {
		player.sendMessage("____________________ H E L P ____________________");
		player.sendMessage("/group help : List group commands.");
		player.sendMessage("/group list : List group members (must be grouped).");
		player.sendMessage("/group invite <Player_Name> : Invite a player to the group.");
		player.sendMessage("/group quit : Quit a group (Disband group if you are its chief).");
		if (isGrouped(player) && isChief(player)) {
			player.sendMessage("/group kick <Player_Name> : Kick a player from the group.");
		}
	}
	
	private static void showList ( Player player ) {
		player.sendMessage("____________________ L I S T ____________________");
		if (isGrouped(player)) {
			player.sendMessage(whichGroup(player).toString());
		}
		else {
			player.sendMessage(ChatColor.RED + "/!\\ You must be in a group to use this command.");
		}
	}
	
	private static void quitGroup ( Player player ) {
		player.sendMessage("____________________ Q U I T ____________________");
		if (!isGrouped(player)) {
			player.sendMessage(ChatColor.RED + "/!\\ You must be in a group to use this " + "command.");
		}
		else if (isChief(player)) {
			// TODO : Text component pour demander au chef de group s'il est sûr de vouloir disband
			disbandGroup(player);
		}
		else {
			Group g = whichGroup(player);
			g.removeMember(player);
			player.sendMessage(ChatColor.YELLOW + "/!\\ You left the group.");
			g.getChief().sendMessage(ChatColor.RED + player.getName() + "left the group");
			if (g.getPlebeians().size() == 0) {
				disbandGroup(player);
			}
		}
	}
	
	private static void disbandGroup ( Player player ) {
		getInstance().groups.remove(whichGroup(player));
		player.sendMessage(ChatColor.RED + "/!\\ The group has been disbanded.");
	}
	
	private static void invitePlayer ( Player chief, Player invited ) {
		// TODO : empecher le joueur de s'inviter lui-même
		// TODO : empecher la creation de doublons
		chief.sendMessage("__________________ I N V I T E __________________");
		if (isGrouped(invited)) {
			chief.sendMessage(ChatColor.YELLOW + "/!\\ The player is already in a group.");
			return;
		}
		if (isGrouped(chief)) {
			if (!isChief(chief)) {
				chief.sendMessage(ChatColor.RED + "You must be group chief to use this command.");
				return;
			}
		}
		getInstance().getInvitations().add(new Invitation(chief, invited));
	}
	
	private static void kickPlayer ( Player chief, Player kicked ) {
		chief.sendMessage("____________________ K I C K ____________________");
		if (!isChief(chief)) {
			chief.sendMessage(ChatColor.RED + "You must be group chief to use this command.");
		}
		else if (chief.equals(kicked)) {
			chief.sendMessage(ChatColor.YELLOW + "You can't kick yourself, use \"/group quit\" next time");
			quitGroup(chief);
		}
		else if (kicked == null || !whichGroup(chief).contains(kicked)) {
			chief.sendMessage(ChatColor.YELLOW + "The player is not in your group.");
		}
		else {
			Group g = whichGroup(chief);
			g.removeMember(kicked);
			chief.sendMessage(ChatColor.YELLOW + kicked.getName() + " has been kicked.");
			kicked.sendMessage(ChatColor.RED + "You have been kicked from the group.");
			if (g.getPlebeians().size() == 0) {
				disbandGroup(chief);
			}
		}
	}
	
	private static void createGroup ( Invitation inv ) {
		Group g = new Group(inv.getSender(), inv.getReceiver());
		getInstance().getGroups().add(g);
	}
	
	private static void acceptInvite ( int hash, Player player ) {
		Invitation inv = getInvFromHash(hash);
		if (inv == null) {
			player.sendMessage("This group invitation doesn't exist or has expired.");
		}
		else if (inv.getExp_date().compareTo(Calendar.getInstance()) < 0) {
			inv.getSender().sendMessage("This group invitation doesn't exist or has expired.");
			getInstance().getInvitations().remove(inv);
		}
		else if (isGrouped(player)) {
			player.sendMessage("You can't accept a group offer when you already are in a group.");
		}
		else if (isGrouped(inv.getSender()) && !isChief(inv.getSender())) {
			player.sendMessage("The player who invited you is no longer a group's chief.");
			getInstance().getInvitations().remove(inv);
		}
		else {
			Group g = whichGroup(inv.getSender());
			if (g == null) {
				createGroup(inv);
			}
			else {
				g.addMember(inv.getReceiver());
			}
			getInstance().getInvitations().remove(inv);
		}
	}
	
	private static void refuseInvitation ( int hash, Player player ) {
		Invitation inv = getInvFromHash(hash);
		if (inv != null && inv.getExp_date().compareTo(Calendar.getInstance()) >= 0) {
			inv.getSender().sendMessage(ChatColor.YELLOW + inv.getReceiver().getName() + " refused the group " +
				"invitation.");
			player.sendMessage("You refused " + inv.getSender() + "'s invitation.");
			getInstance().getInvitations().remove(inv);
		}
		else {
			player.sendMessage("This group invitation doesn't exist or has expired.");
		}
	}
	
	
	@Override
	public boolean onCommand ( CommandSender sender, Command command, String label, String[] args ) {
		if (label.equalsIgnoreCase("group")) {
			if (sender instanceof Player) {
				Player playerSender = (Player) sender;
				Player target;
				
				if ((args.length == 0) || (args[0].equalsIgnoreCase("help")) || (args[0].equalsIgnoreCase("h"))) {
					showHelp(playerSender);
				}
				else if (args.length == 1) {
					switch (args[0].toLowerCase()) {
						case "list":
						case "l":
							showList(playerSender);
							break;
						case "quit":
						case "q":
							quitGroup(playerSender);
							break;
					}
				}
				else if (args.length == 2) {
					int hash;
					switch (args[0].toLowerCase()) {
						case "invite":
						case "i":
							target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								playerSender.sendMessage(ChatColor.RED + "The player you try to invite is " +
									"disconnected " +
								"or doesn't exist.");
								break;
							} invitePlayer(playerSender, target);
							break;
						case "kick":
						case "k":
							target = Bukkit.getPlayer(args[1]);
							kickPlayer(playerSender, target);
							break;
						case "accept":
							hash = Integer.parseInt(args[1]);
							acceptInvite(hash, playerSender);
							break;
						case "refuse":
							hash = Integer.parseInt(args[1]);
							refuseInvitation(hash, playerSender);
							break;
					}
				} return true;
			}
			
			
			return true;
		}
		
		return false;
	}
}
