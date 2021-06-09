import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class Group {
	private Player       chief;
	private List<Player> plebeians;
	
	public Group ( Player chief, Player player ) {
		this.chief = chief;
		this.plebeians.add(player);
	}
	
	public boolean contains ( Player p ) {
		return this.getChief().equals(p) || this.getPlebeians().contains(p);
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder("Players of the group :\n");
		sb.append(this.getChief().getName() + "\n");
		for (Player p : this.getPlebeians()) {
			sb.append(p.getName() + "\n");
		}
		return sb.toString();
	}
}
