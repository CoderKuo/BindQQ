package cn.ctcraft.bindqq.service;

import cn.ctcraft.bindqq.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;
import java.util.Set;

public class BindService {
    private static BindService instance = new BindService();
    public static BindService getInstance(){
        return instance;
    }
    private BindService(){

    }

    public String getQQName(long qq){
        String s = Util.httpRequest("https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq);
        String replace = s.replace("portraitCallBack(", "").replace(")", "");
        JsonObject asJsonObject = new JsonParser().parse(replace).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = asJsonObject.entrySet();
        if(entries.iterator().hasNext()){
            String key = entries.iterator().next().getKey();
            JsonArray asJsonArray = asJsonObject.getAsJsonArray(key);
            JsonElement jsonElement = asJsonArray.get(6);
            String asString = jsonElement.getAsString();
            if(!asString.equals("")){
                return asString;
            }
        }
        return null;
    }

    public boolean checkQQ(Long qq){
        String s = String.valueOf(qq);
        return s.matches("[1-9][0-9]{4,10}");
    }
}
