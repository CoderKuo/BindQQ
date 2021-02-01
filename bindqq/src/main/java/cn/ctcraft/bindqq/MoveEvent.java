package cn.ctcraft.bindqq;

import cn.ctcraft.bindqq.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class MoveEvent implements Listener {
    Database database;
    Bindqq bindqq;
    public MoveEvent(){
        bindqq = Bindqq.getPlugin(Bindqq.class);
        database = bindqq.database;
    }

    @EventHandler
    public void Move(PlayerMoveEvent e){
        Player player = e.getPlayer();
        String qq = database.getQQ(player.getUniqueId().toString());
        if(qq==null){
            String replace = bindqq.langConfig.getString("Message.Success.7").replace("&", "§");
            player.sendMessage(replace);
            e.setCancelled(true);
        }
    }
}
