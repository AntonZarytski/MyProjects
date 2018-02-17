package server.interfaces;

import java.sql.SQLException;

public interface Authorisable {
    boolean registration(String login, String password, String mail) throws SQLException;

    boolean authorisation(String login, String password) throws SQLException;

    boolean checkLoginBusy(String login) throws SQLException;

    boolean checkMailBusy(String mail) throws SQLException;

    void remove(String login) throws SQLException;
}
