package net.kokoricraft.tnttag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.kokoricraft.tnttag.TntTag;

public class PlayerListener implements Listener{
	
	
	TntTag plugin;
	
	public PlayerListener(TntTag plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;
		
		Player damager = (Player)event.getDamager();
		Player victim = (Player)event.getEntity();
		
		if(!plugin.getManager().tagged_players.contains(damager) || plugin.getManager().tagged_players.contains(victim))
			return;
		
		if(!plugin.getManager().players.contains(victim))
			return;
		
		plugin.getManager().unTagg(damager);
		plugin.getManager().taggPlayer(victim);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(plugin.getManager().tagged_players.contains(player)) {
			plugin.getManager().explodePlayer(player);
		}
	}
	
}
