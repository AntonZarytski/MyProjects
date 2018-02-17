package client.Interfaces;

import java.io.IOException;

public interface Communicable {
    String sendAuth(String login, String password) throws IOException, ClassNotFoundException;

    boolean checkLogin(String login) throws IOException, ClassNotFoundException;

    boolean checkMail(String mail) throws IOException, ClassNotFoundException;

    String sendReg(String login, String password, String mail) throws IOException, ClassNotFoundException;
}
