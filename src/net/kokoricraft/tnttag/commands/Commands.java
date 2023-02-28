package net.kokoricraft.tnttag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kokoricraft.tnttag.TntTag;
import net.kokoricraft.tnttag.enums.PlayerMode;

public class Commands implements CommandExecutor {
	TntTag plugin;
	
	public Commands(TntTag plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if(!sender.hasPermission("tnttag.admin")) {
			plugin.getUtils().sendMessage(sender, "&cNo tienes acceso a este comando");
			return true;
		}
		
		if(split.length == 0) {
			plugin.getUtils().sendMessage(sender, "&cArgumentos insuficientes.");
			return true;
		}
		
		switch(split[0].toLowerCase()) {
		case "ronda":
			return roundCommand(sender, label, split);
			
		case "setmode":
			return setModeCommand(sender, label, split);
			
		case "reload":
			plugin.loadConfig();
			plugin.getUtils().sendMessage(sender, "&aConfiguracion recargada con exito!");
			return true;
		}
		
		plugin.getUtils().sendMessage(sender, "&cComando desconocido");
		return true;
	}
	
	private boolean setModeCommand(CommandSender s, String l, String[] a) {
		if(a.length == 1) {
			plugin.getUtils().sendMessage(s, "&cUsa /"+l+" setmode <permission/gamemode>");
			return true;
		}
		
		String modeString = a[1];
		
		PlayerMode mode = modeString.equalsIgnoreCase("permission") ? PlayerMode.PERMISSION : modeString.equalsIgnoreCase("gamemode") ? PlayerMode.GAMEMODE : null;
		
		if(mode == null) {
			plugin.getUtils().sendMessage(s, "&cArgumento equivocado: "+modeString);
			return true;
		}
		
		plugin.getConfigManager().player_mode = mode;
		plugin.getConfigManager().config.set("modo_de_juego.modo", mode.toString());
		plugin.getConfigManager().config.saveConfig();
		plugin.getUtils().sendMessage(s, "&aModo cambiado con exito.");
		
		return true;
	}
	
	private boolean roundCommand(CommandSender s, String l, String[] a) {
		if(a.length == 1) {
			plugin.getUtils().sendMessage(s, "&7| &3[] opcional. <> requisito &7|");
			plugin.getUtils().sendMessage(s, "&e");
			plugin.getUtils().sendMessage(s, "&e/"+l+" ronda iniciar [cantidad] &a- Inicia la ronda.");
			plugin.getUtils().sendMessage(s, "&e/"+l+" ronda forzar <jugador> &a- Fuerza a un jugador ser la papa.");
			plugin.getUtils().sendMessage(s, "&e/"+l+" ronda tiempo <cantidad> &a- Cambia la duracion de la ronda.");
			return true;
		}
		
		
		switch(a[1].toLowerCase()) {
		case "iniciar":{
			if(a.length == 2) {
				plugin.getManager().initRound(s, 1);
			}else {
				if(!plugin.getUtils().isInt(a[2])) {
					plugin.getUtils().sendMessage(s, "&cDebes ingresar un numero");
					break;
				}
				
				plugin.getManager().initRound(s, Integer.parseInt(a[2]));
				plugin.getUtils().sendMessage(s, "&aRonda iniciada.");
			}
			break;
		}
		
		
		case "forzar":{
			if(a.length == 2) {
				plugin.getUtils().sendMessage(s, "&cDebes ingresar un jugador");
				break;
			}
			
			Player player = Bukkit.getPlayerExact(a[2]);
			
			if(player == null) {
				plugin.getUtils().sendMessage(s, "&cEl jugador ingresado no esta conectado");
				break;
			}
			if(plugin.getManager().started_game) {
				plugin.getManager().taggPlayer(player);
			}else {
				if(!plugin.getManager().pre_tagged_players.contains(player))
					plugin.getManager().pre_tagged_players.add(player);
			}
			plugin.getUtils().sendMessage(s, "&aSe ha agregado a la lista de papas calientes.");
			break;
		}
		
		
		case "tiempo":{
			if(a.length == 2) {
				plugin.getUtils().sendMessage(s, "&cDebes ingresar un tiempo");
				break;
			}
			
			String pre_time = a[2];
			
			String time_type = pre_time.substring(pre_time.length()-1, pre_time.length()).toLowerCase();
			
			if(!plugin.getUtils().isInt(pre_time.substring(0, pre_time.length()-1)) || (!time_type.equals("s") && !time_type.equals("m") && !time_type.equals("h"))) {
				plugin.getUtils().sendMessage(s, "&cNo es un formato valido. usa: 10s para 10 segundos, 10m para 10 minutos o 10h para 10 horas");
				break;
			}
			
			int time = Integer.parseInt(pre_time.substring(0, pre_time.length()-1));
			
			switch(time_type) {
			case "m":
				time = time * 60;
				break;
				
			case "h":
				time = (time * 60) * 60;
			}
			
			plugin.getConfigManager().round_max_time = time;
			plugin.getUtils().sendMessage(s, "&aSe establecio el tiempo en: "+time+" segundos");
			plugin.getConfigManager().config.set("segundos_por_ronda", time);
			plugin.getConfigManager().config.saveConfig();
			break;
			}
		}
		return true;
	}
	
	
}
