package net.kokoricraft.tnttag;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.kokoricraft.tnttag.commands.Commands;
import net.kokoricraft.tnttag.commands.CommandsCompleter;
import net.kokoricraft.tnttag.listeners.PlayerListener;
import net.kokoricraft.tnttag.manager.ConfigManager;
import net.kokoricraft.tnttag.manager.Manager;
import net.kokoricraft.tnttag.utils.Utils;

public class TntTag extends JavaPlugin{
	
	private Manager manager;
	private ConfigManager configmanager;
	private Utils utils;
	
	public void onEnable() {
		initClass();
		loadConfig();
		initCommands();
		initListeners();
	}
	
	public void loadConfig() {
		configmanager.loadConfig();
	}
	
	public void initCommands() {
		getCommand("tnttag").setExecutor(new Commands(this));
		getCommand("tnttag").setTabCompleter(new CommandsCompleter(this));
	}
	
	public void initClass() {
		utils = new Utils(this);
		manager = new Manager(this);
		configmanager = new ConfigManager(this);
	}
	
	public void initListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
	}
	
	public Utils getUtils() {
		return utils;
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public ConfigManager getConfigManager() {
		return configmanager;
	}
	
}
