package cn.ctcraft.bindqq.database;

import cn.ctcraft.bindqq.Bindqq;
import com.google.gson.*;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class JsonBase implements Database {
    private File file;
    Bindqq bindqq;

    public JsonBase(){
        bindqq = Bindqq.getPlugin(Bindqq.class);
        load();
    }

    @Override
    public void load() {
        File file = new File(bindqq.getDataFolder() + "/data.json");
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

    private JsonObject getJsonObject(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(stringBuilder.toString());
        if(parse.isJsonNull()){
            return new JsonObject();
        }
        return parse.getAsJsonObject();
    }

    private boolean saveJson(JsonObject jsonObject){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
            Gson gson = new Gson();
            String s = gson.toJson(jsonObject);
            out.write(s.getBytes());
            out.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean saveQQ(String uuid, Long qq) {
        JsonObject jsonObject = getJsonObject();
        jsonObject.addProperty(uuid,qq);
        return saveJson(jsonObject);
    }

    @Override
    public String getQQ(String uuid) {
        JsonObject jsonObject = getJsonObject();
        JsonElement jsonElement = jsonObject.get(uuid);
        return jsonElement.getAsString();
    }

    @Override
    public boolean del(String uuid) {
        JsonObject jsonObject = getJsonObject();
        JsonElement remove = jsonObject.remove(uuid);
        saveJson(jsonObject);
        return remove != null;
    }

    @Override
    public boolean setQQ(String uuid, Long qq) {
        JsonObject jsonObject = getJsonObject();
        jsonObject.addProperty(uuid,qq);
        return saveJson(jsonObject);
    }

    @Override
    public String getName(Long qq) {
        JsonObject jsonObject = getJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            long asLong = entry.getValue().getAsLong();
            if (asLong == qq){
                return entry.getKey();
            }
        }
        return null;
    }
}
