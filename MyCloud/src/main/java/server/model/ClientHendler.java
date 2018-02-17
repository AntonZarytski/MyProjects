package server.model;

import client.model.FileMessage;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ClientHendler {
    public static final String REG_COMAND = "/reg";
    public static final String AUTH_COMAND = "/auth";
    public static final String CHECK_MAIL_COMAND = "/checkMail";
    public static final String CHECK_LOGIN_COMAND = "/checkLogin";
    public static final String GETPATH_COMAND = "/getpath";
    public static final String DELETE_COMAND = "/delete";
    public static final String GETFILE_COMAND = "/getfile";
    public static final String CREATEFOLDER_COMAND = "/createfolder";
    public static final String RENAME_COMAND = "/rename";
    public static final String REGOK = "/regok";
    public static final String MAIL_BUSY = "mailBusy";
    public static final String LOGIN_BUSY = "loginBusy";
    public static final String OK_COMMAND = "/ok ";
    public static final String FALSE_COMMAND = "/false";
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private Object request;
    private FileManager fm;
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
                                if (question.startsWith(REG_COMAND)) {
                                    registration(questions);
                                    String newFolder = db.getRootFolderName(questions[1], questions[2]);
                                    System.out.println(newFolder);
                                    fm.createFolder("../cloud/"+newFolder);
                                    continue;
                                }
                                if (question.startsWith(AUTH_COMAND)) {
                                    if(autorisation(questions))
                                        break;
                                }
                                if (question.startsWith(CHECK_MAIL_COMAND)) {
                                    sendMsg(db.checkMailBusy(questions[1]));
                                    continue;
                                }
                                if (question.startsWith(CHECK_LOGIN_COMAND)) {
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
                                if (question[0].equals(GETPATH_COMAND)) {
                                    fm.sendPath(question[1], objOut);
                                }
                            }
                            if (request instanceof Queue){
                                Queue<String> queue = (PriorityQueue<String>) request;
                                while (!queue.isEmpty()){
                                    String command = queue.poll();
                                    String[] commands = command.split(" ");
                                    if (commands[0].startsWith(DELETE_COMAND)){
                                        fm.deleteFile(new File(commands[1]));
                                    }else if (commands[0].startsWith(GETFILE_COMAND)){
                                        fm.sendFile(commands[1], commands[2], objOut);
                                    }else if (commands[0].startsWith(CREATEFOLDER_COMAND))
                                        fm.createFolder(commands[1]);
                                    else if (commands[0].startsWith(RENAME_COMAND))
                                        fm.renameFile(commands[1], commands[2]);
                                }
                            }
                            if (request instanceof FileMessage)
                                fm.getFile((FileMessage) request);
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
                    sendMsg(REGOK);
                }
            } else {
                sendMsg(MAIL_BUSY);
            }
        } else {
            sendMsg(LOGIN_BUSY);
        }
    }

    private boolean autorisation(String[] questions) throws SQLException, IOException {
        if (db.authorisation(questions[1], questions[2])) {
            sendMsg(OK_COMMAND +db.getRootFolderName(questions[1], questions[2]));
            return true;
        } else {
            sendMsg(FALSE_COMMAND);
            return false;
        }
    }
}
