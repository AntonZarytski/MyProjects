package Server;
import java.sql.*;

class DB {
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;
    private static PreparedStatement ps;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName ( "org.sqlite.JDBC" );
        connection = DriverManager.getConnection ( "jdbc:sqlite:UserData.db" );
        stmt = connection.createStatement ();
        System.out.println ("Подключено к БД");
    }
    public static void disconect() throws SQLException {
        try {
            connection.close ();
        } catch (SQLException e) {
            e.printStackTrace ();
        }finally {
            connection.close();
        }
    }
    public static String getNickbyLoginPassword(String login, String password) throws SQLException {
            ps=connection.prepareStatement ( "SELECT nick FROM UserData WHERE login = ? and password = ?" );
            ps.setString ( 1,login );
            ps.setString ( 2, password );
            rs = ps.executeQuery ();
            if(rs.next ()){
                String nick = rs.getString(1);
                System.out.println (nick);
                return nick;
            }else return null;

    }

    public static  boolean isLoginBusy(String login) throws SQLException {
        ps=connection.prepareStatement("SELECT login FROM UserData WHERE login = ?");
        ps.setString(1,login);
        rs= ps.executeQuery();
        if (rs.equals(login))return true;
        else return false;
    }
    public static boolean chekLoginPass(String login, String password) throws SQLException {
            ps=connection.prepareStatement ( "SELECT login and password FROM UserData WHERE login = ? and password = ?" );
            ps.setString ( 1,login );
            ps.setString ( 2, password );
            rs = ps.executeQuery ();
           if (rs.next ())return true;
           else return false;
    }
    public static void createNewUser(String login, String password, String nick){
        try {
            ps=connection.prepareStatement ("INSERT INTO UserData (login, password, nick) VALUES (?, ?, ?)");
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, nick);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
