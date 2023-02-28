package net.kokoricraft.tnttag.objects;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlaySound {
	private Sound sound;
	private float volume;
	private float pitch;
	
	public PlaySound(ConfigurationSection section) {
		try {
			sound = Sound.valueOf(section.getString("sound"));
			volume = (float) section.getDouble("volume");
			pitch = (float) section.getDouble("pitch");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void play(Player player) {
		if(sound == null)
			return;
		
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
}
