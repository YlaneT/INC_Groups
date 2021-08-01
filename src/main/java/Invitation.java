import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Calendar;

@Getter
public class Invitation {
	private static final int      expiration_time = 1;
	private              Calendar date;
	private              Calendar exp_date;
	private              Player   sender;
	private              Player   receiver;
	
	
	public Invitation ( Player sender, Player receiver ) {
		this.sender = sender;
		this.receiver = receiver;
		this.date = Calendar.getInstance();
		this.exp_date = (Calendar) date.clone();
		exp_date.add(Calendar.MINUTE, expiration_time);
		this.sender.sendMessage("You have invited " + this.receiver.getName() + " to your party");
		this.receiver.sendMessage(org.bukkit.ChatColor.GREEN + "You received a party invitation from " + this.sender.getName());
		
		TextComponent accept = new TextComponent("ACCEPT the invitation ?");
		TextComponent refuse = new TextComponent("REFUSE the invitation ?");
		
		accept.setColor(ChatColor.GREEN);
		accept.setBold(true);
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + this.hashCode()));
		
		refuse.setColor(ChatColor.RED);
		refuse.setBold(true);
		refuse.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group refuse " + this.hashCode()));
		
		
		this.receiver.spigot().sendMessage(accept);
		this.receiver.spigot().sendMessage(refuse);
	}
	
	public boolean contains_sender ( Player player ) {
		return this.sender.equals(player);
	}
	
	public boolean contains_receiver ( Player player ) {
		return this.receiver.equals(player);
	}
	
	public boolean contains ( Player player ) {
		return this.contains_receiver(player) || this.contains_sender(player);
	}
	
	@Override
	public String toString () {
		return "sender : " + sender.getName() + " / receiver : " + receiver.getName();
	}
}
