package cn.ctcraft.bindqq;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KickTimer extends BukkitRunnable {
    private Player player;
    public KickTimer(Player player){
        this.player = player;
    }
    @Override
    public void run() {
        Bindqq plugin = Bindqq.getPlugin(Bindqq.class);
        String qq = plugin.database.getQQ(player.getUniqueId().toString());
        if(qq == null){
            player.kickPlayer(plugin.langConfig.getString("Message.Success.12").replace("&","§"));
            cancel();
        }else {
            cancel();
        }
    }
}
