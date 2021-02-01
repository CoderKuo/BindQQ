package cn.ctcraft.bindqq;

import cn.ctcraft.bindqq.service.BindService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BindCommands implements CommandExecutor {
    private static BindCommands instance = new BindCommands();
    private static Map<Player, qqEntity> data = new HashMap<>();
    private static Map<Player, Integer> codeMap = new HashMap<>();
    BindService bindService;
    Bindqq bindqq;

    public static BindCommands getInstance() {
        return instance;
    }

    private BindCommands() {
        bindService = BindService.getInstance();
        bindqq = Bindqq.getPlugin(Bindqq.class);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bqq")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c§l[BindQQ] 该命令只允许玩家使用!");
                return true;
            }
            if(args.length == 1){
                BindExecute((Player) sender, args);
            }else{
                List<String> lang = bindqq.langConfig.getStringList("Message.help");
                for (String s : lang) {
                    sender.sendMessage(s);
                }
            }
        }
        if (label.equalsIgnoreCase("cname")) {
            String cname = cname(args[0]);
            if (cname != null) {
                String string = bindqq.langConfig.getString("Message.Success.9");
                sender.sendMessage(string.replace("&","§").replace("%name%",args[0]).replace("%QQ%",String.valueOf(cname)));
            } else {
                String string = bindqq.langConfig.getString("Message.Success.8");
                String replace = string.replace("&", "§");
                sender.sendMessage(replace);
            }
        }
        if (label.equalsIgnoreCase("cqq")) {
            String cqq ="";
            try {
               cqq = cqq(Long.valueOf(args[0]));
            }catch (NumberFormatException e){
                sender.sendMessage("§c§l[BindQQ] 参数错误,正确使用方式为: /cqq [qq号]");
                return true;
            }
            if (cqq != null) {
                String string = bindqq.langConfig.getString("Message.Success.10");
                sender.sendMessage(string.replace("&","§").replace("%name%",cqq).replace("%QQ%",args[0]));
            } else {
                String string = bindqq.langConfig.getString("Message.Success.11");
                String replace = string.replace("&", "§");
                sender.sendMessage(replace);
            }

        }

        return true;
    }

    private String cqq(Long qq) {
        String uuid = bindqq.database.getName(qq);
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player != null){
            return player.getDisplayName();
        }else{
            return null;
        }
    }

    private String cname(String name) {
        Player player = Bukkit.getPlayer(name);
        if(player == null){
            return null;
        }
        String s = player.getUniqueId().toString();
        return bindqq.database.getQQ(s);
    }

    private void BindExecute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("ok")) {
            BindExecuteOk(player);
            return;
        }
        if (data.containsKey(player)) {
            player.sendMessage("§c§l[BindQQ] 请输入/bqq ok 确认绑定!");
            return;
        }
        Long qq = new Long(args[0]);
        String name = bindqq.database.getName(qq);
        if(name != null){
            player.sendMessage("§c§l[BindQQ] 请勿重复绑定!");
            return;
        }

        boolean b = bindService.checkQQ(qq);
        if (!b) {
            player.sendMessage("§c§l[BindQQ] QQ号输入有误!");
            return;
        }
        String qqName = bindService.getQQName(qq);
        qqEntity qqEntity = new qqEntity(qq, qqName);
        data.put(player, qqEntity);
        int code = (int) ((Math.random() * 9 + 1) * 1000);
        codeMap.put(player, code);
        player.sendMessage("§e§l[BindQQ] 您的QQ名称为 §f" + qqName);
        player.sendMessage("§e§l[BindQQ] 请将QQ名称后加上 §f§n " + code + " §e§l 后输入/bqq ok 确认绑定！");
    }

    private void BindExecuteOk(Player player) {
        if (!data.containsKey(player)) {
            player.sendMessage("§c§l[BindQQ] 请先输入/bqq [qq号]绑定qq");
            return;
        }
        qqEntity qqEntity = data.get(player);
        Long qqId = qqEntity.getQqId();
        String qqName = bindService.getQQName(qqId);
        Integer integer = codeMap.get(player);
        if (qqName.contains(String.valueOf(integer))) {
            boolean b = bindqq.database.saveQQ(player.getUniqueId().toString(), qqId);
            if (b) {
                player.sendMessage("§a§l[BindQQ] 绑定成功!");
            } else {
                player.sendMessage("§a§l[BindQQ] 绑定失败!");
            }
        } else {
            player.sendMessage("§c§[BindQQ] 绑定失败,请使用/bqq [qq号]尝试重新绑定!");
        }
        codeMap.remove(player);
        data.remove(player);
    }
}
