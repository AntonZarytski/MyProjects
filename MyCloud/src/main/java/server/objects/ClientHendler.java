package server.objects;

import server.files.FileManager;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHendler {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private Object request;
    private FileManager fm;
    private List<File> files;
    private boolean isAuth = false;
    private User user;
    private static DB db;

    public ClientHendler(Socket socket) {
        this.socket = socket;
        db = new DB();
        fm = new FileManager();
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                    objOut = new ObjectOutputStream(out);
                    objIn = new ObjectInputStream(in);
                    while (true) {
                           request = new Object();
                         /**авторизация/регистрация*/
                        while (true) {
                            request = objIn.readObject();
                            if (request instanceof String) {
                                String question = request.toString();
                                String[] questions = question.split(" ");
                                if (question.startsWith("/reg")) {
                                    registration(questions);
                                    fm.createFolder(questions[1]);
                                    continue;
                                }
                                if (question.startsWith("/auth")) {
                                    if(autorisation(questions))
                                        fm.sendPath(questions[1], objOut);
                                        break;
                                }
                                if (question.startsWith("/checkMail")) {
                                    sendMsg(db.checkMailBusy(questions[1]));
                                    continue;
                                }
                                if (question.startsWith("/checkLogin")) {
                                    sendMsg(db.checkLoginBusy(questions[1]));
                                }
                            }
                        }
                        /**Работа с файлами*/
                        while (true) {
                            //TODO как избавться от  SocketException: Connection reset, вроде встречался раньше с таким но не помню как починить
                            request = objIn.readObject();
                            if (request instanceof String) {
                                String[] question = request.toString().split(" ");
                                if (question[0].equals("/getpath")) {
                                    fm.sendPath(question[1], objOut);
                                }
                            }
                            if (request instanceof File) {
                                File requestFile = (File) request;
                                System.out.println("Получен файл " + requestFile.getName());
                                if (files.contains(requestFile)) {
                                    files.remove(requestFile);
                                    System.out.println("Количество объектов после удаления " + files.size());
                                } else {
                                    files.add(requestFile);
                                    System.out.println("Количество объектов после добавления " + files.size());
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException | IOException |
                        SQLException e)

                {
                    try {
                        objIn.close();
                        objOut.close();
                    /*    socket.shutdownInput();
                        socket.shutdownOutput();*/
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        });
        // а стоит ли делать его демоном? мне кажется да т.к. при падении сервера все демоны закроются сами
        tr.setDaemon(true);
        tr.start();
    }

    private void sendMsg(Object obj) throws IOException {
        objOut.writeObject(obj);
        objOut.flush();
    }

    private void registration(String[] questions) throws SQLException, IOException {
        if (!db.checkLoginBusy(questions[1])) {
            if (!db.checkMailBusy(questions[3])) {
                if (db.registration(questions[1], questions[2], questions[3])) {
                    sendMsg("/regok");
                }
            } else {
                sendMsg("mailBusy");
            }
        } else {
            sendMsg("loginBusy");
        }
    }

    private boolean autorisation(String[] questions) throws SQLException, IOException {
        if (db.authorisation(questions[1], questions[2])) {
            sendMsg(true);
            return true;
        } else {
            sendMsg(false);
            return false;
        }
    }
}
