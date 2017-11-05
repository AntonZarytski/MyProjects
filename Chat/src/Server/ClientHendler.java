package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHendler {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    private boolean isOnline;
    public ClientHendler(Socket client){
        isOnline=false;
        long a = System.currentTimeMillis ();
        try {
            this.client = client;
            name = "";
            in = new DataInputStream ( client.getInputStream () );
            out = new DataOutputStream ( client.getOutputStream () );
        } catch (IOException e) {
            e.printStackTrace ();
        }
        new Thread (()-> {
               try {
                   sendMsg ( "У вас есть 120 секунд для авторизации" );
                   while (true) {
                       new Thread (()-> {
                           while(true) {
                               long b = System.currentTimeMillis () - a;
                               if (b >= 120000&& !isOnline) {
                                   sendMsg ("Время на авторизациб истекло");
                                   MyServer.unsubscribeMe ( this );
                                   try {
                                       client.close ();
                                       break;
                                   } catch (IOException e) {
                                       e.printStackTrace ();
                                       break;
                                   }
                               }else if (b>120000) break;
                           }
                       }).start ();
                       String inMsg = in.readUTF();
                       if (inMsg.startsWith("/auth")) {
                           String elements[] = inMsg.split(" ");
                           if (DB.chekLoginPass(elements[1], elements[2])) {
                               String nick = DB.getNickbyLoginPassword(elements[1], elements[2]);
                               if (nick != null) {
                                   if (!MyServer.isNickBusy(nick)) {
                                       sendMsg("/authisok");
                                       this.name = nick;
                                       MyServer.sendMsgForAll(this.name + " зашел в чат");
                                       MyServer.broadcastUsersList();
                                       isOnline = true;
                                       MyServer.broadcastCountofUsers();
                                       break;
                                   } else sendMsg("Учетная запись используется");
                               } else sendMsg("Необходимо ввести имя");
                           } else sendMsg("Не верные логин/пароль");
                       }
                       if(inMsg.startsWith("/new")){
                           String elements[] = inMsg.split(" ");
                            if(!DB.isLoginBusy(elements[1])&& elements.length==4){
                                DB.createNewUser(elements[1], elements[2], elements[3]);
                                this.name = elements[3];
                                MyServer.sendMsgForAll(this.name + " зашел в чат");
                                MyServer.broadcastUsersList();
                                isOnline = true;
                                MyServer.broadcastCountofUsers();
                            }else sendMsg("Данный логин уже занят");
                       }else  sendMsg("Необходимо заполнить все поля");
                   }
                   while (true) {
                       String inMsg = in.readUTF ();
                       if (inMsg.equalsIgnoreCase ( "/end" )) break;
                       if (inMsg.startsWith ( "/w" )) {
                           String[] elements = inMsg.split ( " ", 3 );
                           String nameTo = elements[ 1 ];
                           String message = elements[ 2 ];
                           MyServer.sendMsgTo ( this, nameTo, message );
                       } else {
                           MyServer.sendMsgForAll ( this.name + ": " + inMsg );
                       }
                   }
               } catch (IOException e) {
                   e.printStackTrace ();
               } catch (SQLException e) {
                   e.printStackTrace();
               } finally {
                   MyServer.unsubscribeMe(this);
                   try {
                       client.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
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
    public String getName() {
        return name;
    }
}

