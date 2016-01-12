package db;

import java.sql.*;

public class DBManager {
    private Connection con;

    protected DBManager(String username, String password, String databaseName, String host, int port) throws SQLException {
        this.con = DriverManager.getConnection("jdbc:mysql://" + host + ':' + port + '/' + databaseName +
                "?useUnicode=true&characterEncoding=utf-8&characterSetResults=utf8&user=" +
                username + "&password=" + password);
    }

    private static DBManager obj = null;

    public static final String DefaultHost = "127.0.0.1";
    public static final int DefaultPort = 3306;

    public static DBManager connect(String username, String password, String databaseName) throws SQLException {
        return DBManager.connect(username, password, databaseName, DBManager.DefaultHost, DBManager.DefaultPort);
    }

    public static DBManager connect(String username, String password, String databaseName, String host) throws SQLException {
        return DBManager.connect(username, password, databaseName, host, DBManager.DefaultPort);
    }

    public static DBManager connect(String username, String password, String databaseName, String host, int port) throws SQLException {
        if(DBManager.obj == null)
            DBManager.obj = new DBManager(username, password, databaseName, host, port);

        return DBManager.obj;
    }

    public Connection getConnection() {
        return this.con;
    }

    public static PreparedStatement createStatement(String sql) throws SQLException {
        return DBManager.obj.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
