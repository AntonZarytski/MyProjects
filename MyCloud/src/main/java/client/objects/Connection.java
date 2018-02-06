package client.objects;

import client.Interfaces.Communicable;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

public class Connection implements Communicable {
    private final static int PORT = 8189;
    private final static String HOST = "localhost";
    private static String userName;
    private static Connection ourInstance = new Connection();
    private static Socket client;
    private static InputStream in;
    private static OutputStream out;
    private static ObjectInputStream objIn;
    private static ObjectOutputStream objOut;

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
        try
        {
            client = new Socket(HOST, PORT);
            in = client.getInputStream();
            out = client.getOutputStream();
            objIn = new ObjectInputStream(in);
            objOut = new ObjectOutputStream(out);

      /*      Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        //общение с сервером
                    }
                }
            });
            tr.setDaemon(true);
            tr.start();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String sendAuth(String login, String password) throws IOException, ClassNotFoundException {
        String outMsg = "/auth "+ login + " " + password;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof Boolean){
            if ( (Boolean) request) {
                userName = login;
                return "";
            }
            else return "не верный логин/пароль";
        }return "что то не так, ошибка";
    }

    @Override
    public boolean checkLogin(String login) throws IOException, ClassNotFoundException {
        String outMsg = "/checkLogin "+ login;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof Boolean) {
            return (Boolean) request;
        }
        System.out.println("error checkLogin");
        return true;
    }

    @Override
    public boolean checkMail(String mail) throws IOException, ClassNotFoundException {
        String outMsg = "/checkMail "+ mail;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof Boolean) {
            return(Boolean) request;
        }
        System.out.println("error checkMail");
        return true;
    }

    @Override
    public String sendReg(String login, String password, String mail) throws IOException, ClassNotFoundException {
        String outMsg = "/reg "+ login + " " + password + " " + mail;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if(request instanceof String){
            if(request.equals("mailBusy"))
                return "mailBusy";
            if(request.equals("loginBusy"))
                return "loginBusy";
            if(request.equals("/regok"))
                return "regok";
        }
        return "что то не так, ошибка";
    }
    private void sendMsg(Object obj) throws IOException {
        objOut.writeObject(obj);
        objOut.flush();
    }

    public void getPath() throws IOException {
        objOut.writeObject("/getpath " + userName);
        objOut.flush();
    }
    public void sendCommands(Queue queue) throws IOException {
        objOut.writeObject(queue);
        objOut.flush();
    }
    public static ObjectInputStream getObjIn() {
        return objIn;
    }

    public static ObjectOutputStream getObjOut() {
        return objOut;
    }
}
