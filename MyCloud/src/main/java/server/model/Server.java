package server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private final static int PORT = 8189;
    private ServerSocket server;
    private Socket client;
    private static Vector<ClientHendler> clients;

    public Server() {
        try {
            clients = new Vector<>();
            server = new ServerSocket(PORT);
            System.out.println("сервер запущен");
            while (true){
                client = server.accept();
                clients.add(new ClientHendler(client));
                System.out.println("Клиент подключился");
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
}
