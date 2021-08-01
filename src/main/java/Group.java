import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Group {
	private Player       chief;
	private List<Player> plebeians;
	
	public Group ( Player chief, Player player ) {
		this.chief = chief;
		this.plebeians = new ArrayList<>();
		this.addMember(player);
	}
	
	public boolean contains ( Player p ) {
		return this.getChief().equals(p) || this.getPlebeians().contains(p);
	}
	
	public void addMember ( Player p ) {
		this.plebeians.add(p);
		this.getChief().sendMessage(ChatColor.GREEN + p.getName() + " joined your group.");
		p.sendMessage(ChatColor.GREEN + "You joined " + this.getChief().getName() + "'s group");
	}
	
	public void removeMember ( Player p ) {
		this.plebeians.remove(p);
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder(ChatColor.GOLD + this.getChief().getName());
		sb.append(ChatColor.WHITE + "'s group :\nMembers : ");
		String prefix = "";
		for(Player p : this.getPlebeians()) {
			sb.append(prefix + p.getName());
			prefix = ", ";
		}
		return sb.toString();
	}
}
