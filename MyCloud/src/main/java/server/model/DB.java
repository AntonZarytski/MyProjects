package server.model;

import server.interfaces.Authorisable;

import java.sql.*;

class DB implements Authorisable {
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;
    private static PreparedStatement ps;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        stmt = connection.createStatement();
        System.out.println("Подключено к БД");
    }

    public static void disconect() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public DB() {
        try {
            connect();
          /*  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "login TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT NOT NULL)");*/
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registration(String login, String password, String mail) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO users (login, password, email) VALUES (?, ?, ?)");
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, mail);
        if (ps.executeUpdate() == 1) return true;
        else return false;
    }

    @Override
    public boolean authorisation(String login, String password) throws SQLException {
        ps = connection.prepareStatement("SELECT login and password FROM users WHERE login = ? and password = ?");
        ps.setString(1, login);
        ps.setString(2, password);
        rs = ps.executeQuery();
        if (rs.next()) return true;
        else return false;
    }

    public String getRootFolderName(String login, String password) throws SQLException {
        ps = connection.prepareStatement("SELECT id FROM users WHERE login = ? and password = ?");
        ps.setString(1, login);
        ps.setString(2, password);
        rs = ps.executeQuery();
        if (rs.next())
            return rs.getString(1);
        else return null;
    }

    @Override
    public boolean checkLoginBusy(String login) throws SQLException {
        ps = connection.prepareStatement("SELECT login FROM users WHERE login = ?");
        ps.setString(1, login);
        rs = ps.executeQuery();
        if (rs.next()) {
            String req = rs.getString(1);
            if (req.equals(login))
                return true;
            else return false;
        } else return false;
    }

    @Override
    public boolean checkMailBusy(String mail) throws SQLException {
        ps = connection.prepareStatement("SELECT email FROM users WHERE email = ?");
        ps.setString(1, mail);
        rs = ps.executeQuery();
        if (rs.next()) {
            String req = rs.getString(1);
            if (req.equals(mail))
                return true;
            else return false;
        } else return false;
    }

    @Override
    public void remove(String login) throws SQLException {
        ps = connection.prepareStatement("DELITE * FROM users WHERE login = ?");
        ps.setString(1, login);
        ps.executeUpdate();
    }
}