package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHendler {
    Socket client;
    MyServer server;
    DataInputStream in;
    DataOutputStream out;
    public ClientHendler(MyServer server, Socket client){
        this.client = client;
        this.server = server;
        try {
            in = new DataInputStream ( client.getInputStream () );
            out = new DataOutputStream ( client.getOutputStream () );
        } catch (IOException e) {
            e.printStackTrace ();
        }
        new Thread (()-> {
               try {
                   String inMsg;
                   while (true) {
                       inMsg = in.readUTF ();
                       String elements[] = inMsg.split ( " " );
                       System.out.println (elements[0] + elements[1]+elements[2]+elements[3]);
                       if(elements[0].equals("/auth")) {
                           if (elements[ 1 ].equals ( "login" ) && elements[ 2 ].equals ( "pass" )){
                               System.out.println ("авторизация ок");
                               sendMsg ( "/authisok" );
                           break;
                           }else sendMsg ( "Не верный логин/пароль" );
                       }
                   }
                   while (true){
                       inMsg = in.readUTF ();
                       MyServer.sendMsgForAll ( inMsg );
                   }
               } catch (IOException e) {
                   e.printStackTrace ();
               }

        } ).start ();
    }
    public void sendMsg(String msg){
        try {
            out.writeUTF (msg);
            out.flush ();
        }catch (IOException e){
            e.printStackTrace ();
        }
    }
}
