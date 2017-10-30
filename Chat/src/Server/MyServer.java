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
    private static AuthService authService;

    public MyServer (){
        clients = new Vector <> (  );
        try {
            server = new ServerSocket (PORT);
            authService = new BaseAuthService();
            authService.start();
            System.out.println ("Сервер Запущен");
            while (true) {
                client = server.accept ();
                System.out.println ("Клиент подключен");
                subscribeMe(new ClientHendler (client));
            }
        } catch (IOException e) {
            System.out.println ("Не удалось запустить сервер");
            e.printStackTrace ();
                try {
                    clients.remove ( this );
                    server.close ();
                    client.close ();
                } catch (IOException e1) {
                    e1.printStackTrace ();
                }
                authService.stop();
        }
    }

    public static void sendMsgForAll (String msg){
        for (ClientHendler c:clients)
        c.sendMsg ( msg );
    }
    public static void sendMsgTo(ClientHendler from, String to, String msg){
        for (ClientHendler c: clients){
            if (c.getName().equalsIgnoreCase(to)){
                c.sendMsg("Личное сообщение от " + from.getName() + ": " + msg);
                from.sendMsg("Для "+ to + ": "+msg) ;
            }
        }
    }
    public static synchronized boolean isNickBusy(String nick){
        for (ClientHendler c: clients) {
            if (c.getName().equals(nick)) return true;
        }return false;
    }
    public static void broadcastUsersList (){
        StringBuffer sb = new StringBuffer("/userslist");
        for(ClientHendler c:clients){
            sb.append(" " + c.getName());
        }
        for(ClientHendler c: clients){
            c.sendMsg(sb.toString());
        }
    }
    public static void broadcastCountofUsers (){
        StringBuffer sb = new StringBuffer("/count");
        Integer count = clients.size();
        for(ClientHendler c:clients){
            c.sendMsg(sb.toString() +" "+ count.toString());
        }
    }

    public static AuthService getAuthService() {
        return authService;
    }

    public static void subscribeMe (ClientHendler c ){
        clients.add(c);
        broadcastUsersList();
        broadcastCountofUsers();
    }
    public static void unsubscribeMe (ClientHendler c){
        clients.remove(c);
        broadcastUsersList();
        broadcastCountofUsers();
    }
}
