package cn.ctcraft.bindqq.database;

import cn.ctcraft.bindqq.Bindqq;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.Set;


public class PropertiesBase implements Database {
    Bindqq bindqq;
    private File file;

    public PropertiesBase() {
        bindqq = Bindqq.getPlugin(Bindqq.class);
        load();
    }

    @Override
    public void load() {
        File file = new File(bindqq.getDataFolder() + "/data.properties");
        if (!file.exists()) {
            boolean mkdir = false;
            try {
                mkdir = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mkdir) {
                this.file = file;
                bindqq.getLogger().info("§a§l[BindQQ] 数据文件加载成功!");
            } else {
                bindqq.getLogger().info("§c§l[BindQQ] 数据文件加载失败,插件即将关闭!");
                bindqq.getPluginLoader().disablePlugin(bindqq);
            }
        } else {
            this.file = file;
        }
    }

    private Properties getDataProperties() {
        FileInputStream fileInputStream = null;
        BufferedInputStream in = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream(file);
            in = new BufferedInputStream(fileInputStream);
            properties.load(in);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private boolean saveDataProperties(Properties dataProperties) {
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream out = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
            out = new BufferedOutputStream(fileOutputStream);
            dataProperties.store(out, "last save time: " + new Date());
            return true;
        } catch (Exception e) {
            bindqq.getLogger().warning("§c§l[BindQQ] 数据保存失败!");
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean saveQQ(String uuid, Long qq) {
        Properties dataProperties = getDataProperties();
        if (dataProperties != null) {
            dataProperties.put(uuid, String.valueOf(qq));
            return saveDataProperties(dataProperties);
        }
        return false;
    }

    @Override
    public String getQQ(String uuid) {
        Properties dataProperties = getDataProperties();
        if(dataProperties!=null){
            return (String) dataProperties.get(uuid);
        }
        return null;
    }

    @Override
    public boolean del(String uuid) {
        Properties dataProperties = getDataProperties();
        if(dataProperties!=null){
            dataProperties.remove(uuid);
            return saveDataProperties(dataProperties);
        }
        return false;
    }

    @Override
    public boolean setQQ(String uuid, Long qq) {
        Properties dataProperties = getDataProperties();
        if(dataProperties!=null){
            dataProperties.setProperty(uuid, String.valueOf(qq));
            return true;
        }
        return false;
    }

    @Override
    public String getName(Long qq) {
        Properties dataProperties = getDataProperties();
        if(dataProperties!=null){
            Set<Object> strings = dataProperties.keySet();
            for (Object string : strings) {
                Object o = dataProperties.get(string);
                if(o.toString().equalsIgnoreCase(String.valueOf(qq))){
                    return (String) string;
                }
            }
        }
        return null;
    }
}
