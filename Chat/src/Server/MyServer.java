package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyServer {
    private ServerSocket server;
    private Socket client;
    private final static int PORT = 8189;
    private static Vector <ClientHendler> clients;
    public MyServer (){
        clients = new Vector <> (  );
        try {
            server = new ServerSocket (PORT);
            System.out.println ("Сервер Запущен");
            while (true) {
                client = server.accept ();
                System.out.println ("Клиент подключен");
                clients.add(new ClientHendler (this, client));
            }
        } catch (IOException e) {
            System.out.println ("Не удалось запустить сервер");
            e.printStackTrace ();
                try {
                    server.close ();
                    client.close ();
                } catch (IOException e1) {
                    e1.printStackTrace ();
                }
            }
    }
    public static void sendMsgForAll (String msg){
        for (ClientHendler c:clients){
        c.sendMsg ( msg );
        }
    }
}
