package cn.ctcraft.bindqq;

import cn.ctcraft.bindqq.database.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Bindqq extends JavaPlugin {
    Database database;
    YamlConfiguration langConfig;
    String prefix;

    @Override
    public void onEnable() {

        this.getCommand("bqq").setExecutor(BindCommands.getInstance());
        this.getCommand("cqq").setExecutor(BindCommands.getInstance());
        this.getCommand("cname").setExecutor(BindCommands.getInstance());
        File file = new File(getDataFolder() + "/config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        File file1 = new File(getDataFolder() + "/lang.yml");
        if (!file1.exists()) {
            saveResource("lang.yml", false);
        }

        String databaseOption = getConfig().getString("database.type");
        database = setDataBase(databaseOption);
        if (database == null) {
            getLogger().warning("§c§l数据存储配置错误,插件即将停止运行!");
            getPluginLoader().disablePlugin(this);
        }
        langConfig = getLangConfig();

        boolean aBoolean = getConfig().getBoolean("config.move");
        if(aBoolean){
            getServer().getPluginManager().registerEvents(new MoveEvent(),this);
        }
        boolean kick = getConfig().getBoolean("config.kick.use");
        if(kick){
            getServer().getPluginManager().registerEvents(new JoinEvent(),this);
        }

        Metrics metrics = new Metrics(this);

        prefix = langConfig.getString("Message.prefix");

        getLogger().info("§e§l>>>绑定QQ插件加载成功!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private Database setDataBase(String databaseOption) {
        if (databaseOption.equalsIgnoreCase("json")) {
            return new JsonBase();
        }
        if (databaseOption.equalsIgnoreCase("yaml")) {
            return new YamlBase();
        }
        if (databaseOption.equalsIgnoreCase("properties")) {
            return new PropertiesBase();
        }
        if (databaseOption.equalsIgnoreCase("sqlite")) {
            return new SqliteBase();
        }
        if (databaseOption.equalsIgnoreCase("mysql")) {
            ConfigurationSection mysqlConfig = getConfig().getConfigurationSection("database.mysql");
            String host = mysqlConfig.getString("host");
            String port = mysqlConfig.getString("port");
            String database = mysqlConfig.getString("database");
            String user = mysqlConfig.getString("user");
            String password = mysqlConfig.getString("password");
            boolean usessl = mysqlConfig.getBoolean("usessl");
            return new MysqlBase(host, user, password, database, port, usessl);
        }
        return null;
    }

    private YamlConfiguration getLangConfig() {
        File file = new File(getDataFolder() + "/lang.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

}
