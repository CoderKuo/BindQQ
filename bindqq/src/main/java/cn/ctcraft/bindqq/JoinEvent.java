package cn.ctcraft.bindqq;

import cn.ctcraft.bindqq.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class JoinEvent implements Listener {
    Database database;
    Bindqq bindqq;

    public JoinEvent(){
        bindqq = Bindqq.getPlugin(Bindqq.class);
        database = bindqq.database;
    }
    @EventHandler
    public void Join(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String qq = database.getQQ(player.getUniqueId().toString());
        if(qq == null){
            int anInt = bindqq.getConfig().getInt("config.kick.time");
            player.sendMessage("§c§l请在"+anInt+"秒内输入/bqq [qq号]绑定qq,否则将被踢出服务器!");
            KickTimer kickTimer = new KickTimer(player);
            kickTimer.runTaskTimer(bindqq,anInt*20,anInt*20);
        }
    }
}
