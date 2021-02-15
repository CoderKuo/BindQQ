package cn.ctcraft.bindqq.database;

import java.util.List;

public interface Database {
    public void load();

    public boolean saveQQ(String uuid,Long qq);
    public String getQQ(String uuid);
    public boolean del(String uuid);
    public boolean setQQ(String uuid,Long qq);
    public List<String> getName(Long qq);


}
