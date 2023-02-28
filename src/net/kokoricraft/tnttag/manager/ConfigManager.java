package net.kokoricraft.tnttag.manager;

import java.util.List;
import java.util.stream.Collectors;

import net.kokoricraft.tnttag.TntTag;
import net.kokoricraft.tnttag.enums.PlayerMode;
import net.kokoricraft.tnttag.objects.NekoConfig;
import net.kokoricraft.tnttag.objects.PlaySound;

public class ConfigManager {
	
	TntTag plugin;
	
	public NekoConfig config;
	
	public ConfigManager(TntTag plugin) {
		this.plugin = plugin;
	}
	
	public int round_max_time;
	public float explosion_size;
	public PlayerMode player_mode;
	public String player_permission;
	public String spectator_permission;
	public List<String> player_gamemode;
	public String time_format;
	public String tagged_time_format;
	public String tagged_chat_message;
	public String tag_subtitle_message;
	public String eliminated_chat_message;
	public String eliminated_kick_message;
	
	public PlaySound tagged_sound;
	public PlaySound last_seconds_sound;
	
	public void loadConfig() {
		config = new NekoConfig("config.yml", plugin);
		
		round_max_time = config.getInt("segundos_por_ronda", 30);
		explosion_size = (float) config.getDouble("explosion_size", 2);
		
		player_mode = PlayerMode.valueOf(config.getString("modo_de_juego.modo", "PERMISSION").toUpperCase());
		player_permission = config.getString("permission.player", "tnttag.player");
		spectator_permission = config.getString("permissions.spectator", "tnttag.spectator");
		
		List<String> gamemodes = config.getStringList("modo_de_juego.gamemodes", List.of("ADVENTURE", "SURVIVAL"));
		player_gamemode = gamemodes.stream().map(String::toUpperCase).collect(Collectors.toList());
		
		time_format = config.getString("messages.time_format", "&e<hours>&7:&e<minutes>&7:&e<seconds>");
		tagged_time_format = config.getString("messages.tagged_time_format", "&c☠ &e<hours>&7:&e<minutes>&7:&e<seconds> &c☠");
		tagged_chat_message = config.getString("messages.tagged_chat_message", "&7El jugador %player% tiene la papa caliente!");
		tag_subtitle_message = config.getString("messages.tag_subtitle_message", "&cTienes la papa caliente ☠");
		eliminated_chat_message = config.getString("messages.eliminated_chat_message", "&cEl jugador %player% fue eliminado. ☠");
		eliminated_kick_message = config.getString("messages.eliminated_kick_message", "&cHas sido eliminado.");
		config.update();
		
		tagged_sound = new PlaySound(config.getConfigurationSection("sounds.tagged_player"));
		last_seconds_sound = new PlaySound(config.getConfigurationSection("sounds.last_seconds"));
	}
}
