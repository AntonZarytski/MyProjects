package Client;

public class Main {
    private final static int PORT = 8189;
    private final static String HOST = "localhost";
    public static void main(String[] args) {
        ClientWindow windwow = new ClientWindow ("Чат", HOST, PORT);
    }
}
