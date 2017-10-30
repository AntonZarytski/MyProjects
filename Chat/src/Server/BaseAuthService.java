package Server;

import java.sql.SQLException;

public class BaseAuthService implements AuthService{
    private class Entry {
        private String login;
        private String pass;
        private String nick;
        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }
    @Override
    public void start() {
        try {
            DB.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() {
        try {
            DB.disconect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}