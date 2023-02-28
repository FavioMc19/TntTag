package net.kokoricraft.tnttag.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.kokoricraft.tnttag.TntTag;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	
	
	TntTag plugin;
	
	public Utils(TntTag plugin) {
		this.plugin = plugin;
	}
	
	public String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', colorHex(text));
	}
	
	public void sendMessage(CommandSender sender, String text) {
		sender.sendMessage(color(text));
	}
	
	public TextComponent getTextComponent(String text) {
		TextComponent base = new TextComponent();
		
		String color = null;
		for(String part : hexSeparator(text)) {
			if(isHex(part)) {
				color = part;
			}else {
				TextComponent component = new TextComponent(color(part));
				if(color != null)
					component.setColor(net.md_5.bungee.api.ChatColor.of(color.startsWith("&") ? color.substring(1, color.length()) : color));
				base.addExtra(component);
			}
		}
		return base;
	}
	
	public String colorHex(String text) {
		String message = "";
		
		Matcher matcher = hex_separator_pattern.matcher(text);
		
		int index = 0;
		while(matcher.find()) {
			message += text.substring(index, matcher.start()) + net.md_5.bungee.api.ChatColor.of((matcher.group().startsWith("&") ? matcher.group().substring(1, matcher.group().length()) : matcher.group()));
			index = matcher.end();
		}
		return message += text.substring(index, text.length());
	}
	
	Pattern hex_separator_pattern = Pattern.compile("(&#|#)([A-Fa-f0-9]{6})");
	
	public List<String> hexSeparator(String text) {
		List<String> texts = new ArrayList<String>();
		
		 Matcher matcher = hex_separator_pattern.matcher(text);
		 
		 int index = 0;
		 
		 while(matcher.find()) {
			 texts.add(text.substring(index, matcher.start()));
			 texts.add(matcher.group().startsWith("&") ? matcher.group() : "&"+matcher.group());
			 index = matcher.end();
		 }
		 
		 texts.add(text.substring(index, text.length()));
		 return texts;
	}
	
	public Boolean isHex(String text) {
		if(text == null)
			return false;
		
		if(text.startsWith("&"))
			text = text.substring(1, text.length());
		
		 Matcher matcher = hex_separator_pattern.matcher(text);
	 
	     return matcher.matches();
	}
	
	public Boolean isInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	public String getDurationString(int seconds, Boolean tagged) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		String format = tagged ? plugin.getConfigManager().tagged_time_format : plugin.getConfigManager().time_format;
		return format.replaceAll("<hours>", twoDigitString(hours)).replaceAll("<minutes>", twoDigitString(minutes)).replaceAll("<seconds>", twoDigitString(seconds));
	}
	
	private String twoDigitString(int number) {
		if (number == 0)
			return "00";

		if (number / 10 == 0)
			return "0" + number;

		return String.valueOf(number);
	}
	
}
