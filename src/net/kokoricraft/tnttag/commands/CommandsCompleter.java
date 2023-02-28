package net.kokoricraft.tnttag.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.kokoricraft.tnttag.TntTag;

public class CommandsCompleter implements TabCompleter {
	TntTag plugin;
	
	public CommandsCompleter(TntTag plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		if(!s.hasPermission("tnttag.admin"))
			return new ArrayList<String>();
		
		if(a.length == 1)
			return getArguments(List.of("ronda", "setmode", "reload"), a[0]);
		
		switch(a[0].toLowerCase()) {
		case "ronda":
			return roundCompleter(a);
			
		case "setmode":
			return setModeCompleter(a);
		}
		
		return new ArrayList<String>();
	}
	
	private List<String> roundCompleter(String[] a) {
		if(a.length == 2) {
			return getArguments(List.of("iniciar", "forzar", "tiempo"), a[1]);
		}
		
		if(a[1].equalsIgnoreCase("iniciar") && a.length == 3)
			return getArguments(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9"), a[2]);
		
		if(a[1].equalsIgnoreCase("forzar") && a.length == 3)
			return null;
		
		if(a[1].equalsIgnoreCase("tiempo") && a.length == 3) {
			String pre  = a[2];
			
			if(!pre.endsWith("s") && !pre.endsWith("m") && !pre.endsWith("h")) {
				if(pre.equals(""))
					return getArguments(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9"), a[2]);
				
				return getArguments(List.of(pre+"s", pre+"m", pre+"h"), a[2]);
			}
		}
		
		return new ArrayList<String>();
	}
	
	private List<String> setModeCompleter(String[] a){
		if(a.length != 2)
			return new ArrayList<String>();
		
		return getArguments(List.of("permission", "gamemode"), a[1]);
	}

	public List<String> getArguments(List<String> allcommands, String arg){
		if(arg.isBlank() || arg.isEmpty())
			return allcommands;
		
		List<String> endArgs = new ArrayList<String>();
		for(String text : allcommands) {
			if(text.toLowerCase().startsWith(arg.toLowerCase())) {
				endArgs.add(text);
			}
		}
		return endArgs;
	}
}
