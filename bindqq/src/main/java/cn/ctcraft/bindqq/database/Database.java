package cn.ctcraft.bindqq.database;

public interface Database {
    public void load();

    public boolean saveQQ(String uuid,Long qq);
    public String getQQ(String uuid);
    public boolean del(String uuid);
    public boolean setQQ(String uuid,Long qq);
    public String getName(Long qq);


}
