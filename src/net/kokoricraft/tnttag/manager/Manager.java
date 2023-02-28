package net.kokoricraft.tnttag.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.kokoricraft.tnttag.TntTag;
import net.md_5.bungee.api.ChatMessageType;

public class Manager {
	
	TntTag plugin;
	
	public Manager(TntTag plugin) {
		this.plugin = plugin;
		started_game = false;
		current_time = 0;
	}
	
	public int max_tagged_players;
	public int current_time;
	public boolean started_game;
	
	public List<Player> players = new ArrayList<Player>();
	public List<Player> tagged_players = new ArrayList<Player>();
	public List<Player> pre_tagged_players = new ArrayList<Player>();
	
	public void initRound(CommandSender sender, int tag_amount) {
		if(started_game) {
			plugin.getUtils().sendMessage(sender, "&cEl juego ya esta iniciado!");
			return;
		}
		
		max_tagged_players = (tag_amount < 1) ? 1 : tag_amount;
		started_game = true;
		
		fillPlayers();
		
		if(players.size() == 0) {
			plugin.getUtils().sendMessage(sender, "&cJugadores insuficientes.");
			return;
		}
		
		fillTagged();
		
		for(Player tagged : tagged_players) {
			taggPlayer(tagged);
		}
		
		initTask();
	}
	
	public void endRound() {
		task.cancel();
		
		for(Player player : tagged_players) {
			explodePlayer(player);
		}
		
		tagged_players.clear();
		started_game = false;
		current_time = 0;
	}
	
	private BukkitTask task;
	
	public void initTask() {
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				current_time++;
				
				if(current_time >= plugin.getConfigManager().round_max_time || tagged_players.size() == 0) {
					endRound();
					return;
				}
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					int time = plugin.getConfigManager().round_max_time - current_time;
					boolean tagged = tagged_players.contains(player);
					
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, plugin.getUtils().getTextComponent(plugin.getUtils().getDurationString(time, tagged)));
					
					if(time <= 10)
						plugin.getConfigManager().last_seconds_sound.play(player);
				}
			}
			
		}, 0L, 20L);
	}
	
	public void taggPlayer(Player tagged) {
		if(!tagged_players.contains(tagged))
			tagged_players.add(tagged);
		
		tagged.setGlowing(true);
		tagged.sendTitle("", plugin.getUtils().color(plugin.getConfigManager().tag_subtitle_message), 0, 60, 0);
		plugin.getConfigManager().tagged_sound.play(tagged);
		Bukkit.broadcastMessage(plugin.getUtils().color(plugin.getConfigManager().tagged_chat_message.replaceAll("%player%", tagged.getName())));
	}
	
	public void unTagg(Player player) {
		if(!tagged_players.contains(player))
			return;
		
		tagged_players.remove(player);
		player.setGlowing(false);
		player.sendTitle("", "", 0, 60, 0);
	}
	
	public void explodePlayer(Player player) {
		new BukkitRunnable() {
            @Override
            public void run() {
            	player.getWorld().createExplosion(player.getLocation(), plugin.getConfigManager().explosion_size, false, false);
            	player.setGlowing(false);
            	Bukkit.broadcastMessage(plugin.getUtils().color(plugin.getConfigManager().eliminated_chat_message.replaceAll("%player%", player.getName())));
            	if(!player.hasPermission(plugin.getConfigManager().player_permission)) {
    				player.kickPlayer(plugin.getUtils().color(plugin.getConfigManager().eliminated_kick_message.replaceAll("%player%", player.getName())));
    			}else {
    				player.setGameMode(GameMode.SPECTATOR);
    			}
            	tagged_players.remove(player);
            }}.runTask(plugin);
		
	}
	
	private void fillPlayers() {
		switch(plugin.getConfigManager().player_mode) {
		case PERMISSION:
			players = Bukkit.getOnlinePlayers().stream().filter(player -> (
					player.hasPermission(plugin.getConfigManager().player_permission) && !player.getGameMode().equals(GameMode.SPECTATOR))).collect(Collectors.toList());
			break;
		case GAMEMODE:
			players = Bukkit.getOnlinePlayers().stream().filter(player -> plugin.getConfigManager().player_gamemode.contains(player.getGameMode().toString())).collect(Collectors.toList());
			break;
		}
	}
	
	private void fillTagged() {
		if(!pre_tagged_players.isEmpty()) {
			tagged_players.addAll(pre_tagged_players);
			pre_tagged_players.clear();
		}
		
		if(tagged_players.size() >= max_tagged_players)
			return;
		
		for(int i = 0; i < max_tagged_players; i++) {
			if(players.size() == 1) {
				tagged_players.add(players.get(0));
				break;
			}
			
			int index = ThreadLocalRandom.current().nextInt(players.size());
			tagged_players.add(players.get(index));
			
			if(tagged_players.size() >= max_tagged_players)
				break;
		}
	}
	
}
