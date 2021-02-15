package cn.ctcraft.bindqq.database;

import cn.ctcraft.bindqq.Bindqq;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteBase implements Database {
    private String url;
    Bindqq bindqq;


    public SqliteBase() {
        bindqq = Bindqq.getPlugin(Bindqq.class);
        load();
    }

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            Bindqq.getPlugin(Bindqq.class).getLogger().warning("§c§l>>>SQLite数据库连接失败!");
            e.printStackTrace();
        }
        return null;
    }

    //关闭连接
    public void close(ResultSet rs, PreparedStatement ptmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ptmt != null) ptmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //关闭连接
    public void close(ResultSet rs, Statement ptmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ptmt != null) ptmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        url = "jdbc:sqlite:" + bindqq.getDataFolder() + "/data.db";


        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            int i = conn.createStatement().executeUpdate(" CREATE TABLE IF NOT exists `qq_data`  (`uuid` varchar(255) NOT NULL,`qq` int(20),PRIMARY KEY (`uuid`));");
            if (i != -1) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
    }

    @Override
    public boolean saveQQ(String uuid, Long qq) {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "insert into qq_data values (?,?)";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, uuid);
            ptmt.setLong(2, qq);
            int i = ptmt.executeUpdate();
            if (i != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
        return false;
    }

    @Override
    public String getQQ(String uuid) {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "select `qq` from qq_data where `uuid` = ?";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, uuid);
            rs = ptmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
        return null;
    }

    @Override
    public boolean del(String uuid) {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "delete from qq_data where `uuid` = ?";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, uuid);
            int i = ptmt.executeUpdate();
            if (i != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
        return false;
    }

    @Override
    public boolean setQQ(String uuid, Long qq) {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "update qq_data set `qq` = ? where `uuid` = ?";
            ptmt = conn.prepareStatement(sql);
            ptmt.setLong(1, qq);
            ptmt.setString(2, uuid);
            int i = ptmt.executeUpdate();
            if (i != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
        return false;
    }

    @Override
    public List<String> getName(Long qq) {
        List<String> list = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "select `uuid` from qq_data where `qq` = ?";
            ptmt = conn.prepareStatement(sql);
            ptmt.setLong(1, qq);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ptmt, conn);
        }
        return list;
    }
}
