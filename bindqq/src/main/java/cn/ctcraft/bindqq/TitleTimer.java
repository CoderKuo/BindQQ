package cn.ctcraft.bindqq;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleTimer extends BukkitRunnable {
    private Player player;
    public TitleTimer(Player player){
        this.player = player;
    }
    @Override
    public void run() {
        Bindqq plugin = Bindqq.getPlugin(Bindqq.class);
        String qq = plugin.database.getQQ(player.getUniqueId().toString());
        if(qq!=null){
            cancel();
            return;
        }
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection message = config.getConfigurationSection("config.title.message");
        String title = message.getString("title").replace("&","§");
        String subtitle = message.getString("subtitle").replace("&","§");
        int fadeIn = message.getInt("fadeIn");
        int stay = message.getInt("stay");
        int fadeOut = message.getInt("fadeOut");
        player.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
    }
}
