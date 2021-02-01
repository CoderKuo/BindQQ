package cn.ctcraft.bindqq.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class MysqlBase implements Database {
    private Properties info = new Properties();
    private String url;
    private static ArrayList<Connection> pool = new ArrayList<>();

    public MysqlBase(String host,String user,String pass,String database,String port,boolean ssl){
        this.info.put("autoReconnect", "true");
        this.info.put("user", user);
        this.info.put("password", pass);
        this.info.put("useUnicode", "true");
        this.info.put("characterEncoding", "utf8");
        this.info.put("useSSL", ssl);

        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        for(int i = 0; i < 8; ++i) {
            pool.add((Connection) null);
        }
    }

    public Connection getConnection() {

        int i = 0;

        while(i < 8) {
            Connection connection = (Connection)pool.get(i);
            try {
                if (connection != null && !connection.isClosed() && connection.isValid(10)) {
                    return connection;
                }

                connection = DriverManager.getConnection(this.url, this.info);
                pool.set(i, connection);
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                ++i;
            }
        }

        return null;
    }


    //关闭连接
    public void close(ResultSet rs,PreparedStatement ptmt,Connection conn){
        try {
            if(rs!=null)rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(ptmt!=null)ptmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(conn!=null)conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //关闭连接
    public void close(ResultSet rs, Statement ptmt, Connection conn){
        try {
            if(rs!=null)rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(ptmt!=null)ptmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(conn!=null)conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            int i = conn.createStatement().executeUpdate("CREATE TABLE `bind_qq`.`qq_data`  (\n" +
                    "  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '玩家uuid',\n" +
                    "  `qq` int(20) NULL COMMENT '玩家qq',\n" +
                    "  PRIMARY KEY (`uuid`)\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci");
            if(i != -1){
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
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
            ptmt.setString(1,uuid);
            ptmt.setLong(2,qq);
            int i = ptmt.executeUpdate();
            if(i != -1){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
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
            ptmt.setString(1,uuid);
            rs = ptmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
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
            ptmt.setString(1,uuid);
            int i = ptmt.executeUpdate();
            if(i != -1){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
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
            ptmt.setLong(1,qq);
            ptmt.setString(2,uuid);
            int i = ptmt.executeUpdate();
            if(i != -1){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
        }
        return false;
    }

    @Override
    public String getName(Long qq) {
        ResultSet rs = null;
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "select `uuid` from qq_data where `qq` = ?";
            ptmt = conn.prepareStatement(sql);
            ptmt.setLong(1,qq);
            rs = ptmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ptmt,conn);
        }
        return null;
    }
}
