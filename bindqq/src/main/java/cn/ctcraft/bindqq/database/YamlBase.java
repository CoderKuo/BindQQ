package cn.ctcraft.bindqq.database;

import cn.ctcraft.bindqq.Bindqq;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Set;

public class YamlBase implements Database {
    private File file;
    Bindqq bindqq;

    public YamlBase(){
        bindqq = Bindqq.getPlugin(Bindqq.class);
        load();
    }

    @Override
    public void load() {
        file = new File(bindqq.getDataFolder() + "/data.yml");
    }

    public YamlConfiguration getYamlData(){
        return YamlConfiguration.loadConfiguration(file);
    }

    public boolean saveYamlData(YamlConfiguration yamlConfiguration){
        try {
            yamlConfiguration.save(file);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveQQ(String uuid, Long qq) {
        YamlConfiguration yamlData = getYamlData();
        yamlData.set(uuid,qq);
        return saveYamlData(yamlData);
    }

    @Override
    public String getQQ(String uuid) {
        YamlConfiguration yamlData = getYamlData();
        return yamlData.getString(uuid);
    }

    @Override
    public boolean del(String uuid) {
        YamlConfiguration yamlData = getYamlData();
        yamlData.set(uuid,"");
        return saveYamlData(yamlData);
    }

    @Override
    public boolean setQQ(String uuid, Long qq) {
        YamlConfiguration yamlData = getYamlData();
        yamlData.set(uuid,qq);
        return saveYamlData(yamlData);
    }

    @Override
    public String getName(Long qq) {
        YamlConfiguration yamlData = getYamlData();
        Set<String> keys = yamlData.getKeys(false);
        for (String key : keys) {
            long aLong = yamlData.getLong(key);
            if(aLong == qq){
                return key;
            }
        }
        return null;
    }
}
