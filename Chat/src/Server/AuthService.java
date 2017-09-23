package Server;

public interface AuthService {
    void start();
    void stop();
    String getNickByLoginPassword (String login, String password);
}
