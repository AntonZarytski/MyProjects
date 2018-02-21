package client.model;

import client.Interfaces.Communicable;
import client.controllers.WorkWindowControll;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

public class Connection implements Communicable {
    public static final String FALSE_COMMAND = "/false";
    private final static int PORT = 8189;
    private final static String HOST = "localhost";
    public static final String AUTH_COMMAND = "/auth ";
    public static final String OK_COMMAND = "/ok";
    public static final String WRONG_LOGIN_PASSWORD = "не верный логин/пароль";
    public static final String UNKNOWN_ERROR = "что то не так, ошибка";
    public static final String CHECK_LOGIN_COMMAND = "/checkLogin ";
    public static final String CHECK_MAIL_COMMAND = "/checkMail ";
    public static final String ERROR_CHECK_MAIL = "error checkMail";
    public static final String REG_COMMAND = "/reg ";
    public static final String MAIL_BUSY = "mailBusy";
    public static final String LOGIN_BUSY = "loginBusy";
    public static final String REG_OK_COMMAND = "/regok";
    public static final String REGOK = "regok";
    public static final String GETPATH_COMMAND = "/getpath ";
    private static String userId;
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
        try {
            client = new Socket(HOST, PORT);
            in = client.getInputStream();
            out = client.getOutputStream();
            objIn = new ObjectInputStream(in);
            objOut = new ObjectOutputStream(out);

            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        //общение с сервером
                    }
                }
            });
            tr.setDaemon(true);
            tr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendAuth(String login, String password) throws IOException, ClassNotFoundException {
        String outMsg = AUTH_COMMAND + login + " " + password;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof String) {
            String[] answer = ((String) request).split(" ");
            if (answer[0].equals(OK_COMMAND)) {
                userId = answer[1];
                WorkWindowControll.setUserId(userId);
                return "";
            } else if (answer[0].equals(FALSE_COMMAND)) return WRONG_LOGIN_PASSWORD;
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public boolean checkLogin(String login) throws IOException, ClassNotFoundException {
        String outMsg = CHECK_LOGIN_COMMAND + login;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof Boolean) {
            return (Boolean) request;
        }
        return true;
    }

    @Override
    public boolean checkMail(String mail) throws IOException, ClassNotFoundException {
        String outMsg = CHECK_MAIL_COMMAND + mail;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof Boolean) {
            return (Boolean) request;
        }
        System.out.println(ERROR_CHECK_MAIL);
        return true;
    }

    @Override
    public String sendReg(String login, String password, String mail) throws IOException, ClassNotFoundException {
        String outMsg = REG_COMMAND + login + " " + password + " " + mail;
        sendMsg(outMsg);
        Object request = objIn.readObject();
        if (request instanceof String) {
            if (request.equals(MAIL_BUSY))
                return MAIL_BUSY;
            if (request.equals(LOGIN_BUSY))
                return LOGIN_BUSY;
            if (request.equals(REG_OK_COMMAND))
                return REGOK;
        }
        return UNKNOWN_ERROR;
    }

    private void sendMsg(Object obj) throws IOException {
        objOut.writeObject(obj);
        objOut.flush();
    }

    public void getPath() throws IOException {
        objOut.writeObject(GETPATH_COMMAND + userId);
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
