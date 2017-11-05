package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientWindow extends JFrame {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private JPanel authPanel;
    private JPanel sendMsgPanel;
    private JPanel clientListPanel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField nickNameField;
    private JButton sendAuthButton;
    private JTextField msgField;
    private JButton sendMsgButton;
    private JTextArea clientListArea;
    private JTextArea countOfClientsField;
    private JTextArea msgHistoryArea;
    private JPanel startWindow;
    private JButton logIn;
    private JButton newUser;
    private JButton back;
    private boolean isAuthorised;
    private boolean isNewUser;
    private JLayeredPane layer;

    public void setAuthorised(boolean authorised) {
        isAuthorised = authorised;
        layer.setVisible(!authorised);
        authPanel.setVisible (!isAuthorised);
        sendMsgPanel.setVisible ( isAuthorised );
        clientListPanel.setVisible ( isAuthorised );
    }

    public ClientWindow (String tittle, String host, int port){
        try {
            client = new Socket ( host, port );
            in = new DataInputStream ( client.getInputStream () );
            out = new DataOutputStream ( client.getOutputStream () );
        } catch (IOException e) {
            e.printStackTrace ();
        }
        setTitle ( tittle );
        setBounds ( 300,300,640,640 );
        setLayout ( new BorderLayout (  ) );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        layer = new JLayeredPane();
        authPanel = new JPanel ( new GridLayout ( 2,4 ) );
        authPanel.setVisible(false);
        loginField = new JTextField ( "Логин" );
        passwordField = new JPasswordField ( "Пароль" );
        nickNameField = new JTextField ( "Имя" );
        sendAuthButton = new JButton ( "Войти в чат" );
        back = new JButton("Назад");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startWindow.setVisible(true);
                authPanel.setVisible(false);
            }
        });
        authPanel.add(loginField);
        authPanel.add(passwordField);
        authPanel.add(nickNameField);
        authPanel.add(sendAuthButton);
        authPanel.add(back);
        add(authPanel, BorderLayout.NORTH);
        sendAuthButton.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isNewUser) {
                    sendMsg("/auth" + " " + loginField.getText() + " " + passwordField.getText() + " " + nickNameField.getText());
                    loginField.setText("");
                    passwordField.setText("");
                    nickNameField.setText("");
                }else {
                    sendMsg("/new" + " " + loginField.getText() + " " + passwordField.getText() + " " + nickNameField.getText());
                    loginField.setText("");
                    passwordField.setText("");
                    nickNameField.setText("");
                }
            }
        } );
        loginField.addFocusListener ( new FocusListener () {
            @Override
            public void focusGained(FocusEvent e) {
                if (loginField.getText ().equals ( "Логин" ))
                    loginField.setText ( "" );
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (loginField.getText ().isEmpty ())
                    loginField.setText ( "Логин" );
            }
        } );
        passwordField.addFocusListener ( new FocusListener () {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText ().equals ( "Пароль" ))
                    passwordField.setText ( "" );
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText ().isEmpty ())
                    passwordField.setText ( "Пароль" );
            }
        } );
        nickNameField.addFocusListener ( new FocusListener () {
            @Override
            public void focusGained(FocusEvent e) {
                if (nickNameField.getText ().equals ( "Имя" ))
                    nickNameField.setText ( "" );
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nickNameField.getText ().isEmpty ())
                    nickNameField.setText ( "Имя" );
            }
        } );
        sendMsgPanel = new JPanel (new GridLayout(1,2)  );
        msgField = new JTextField ( "Отправить сообщение" );
        sendMsgButton = new JButton ( "Отправить" );
        sendMsgPanel.add(msgField);
        sendMsgPanel.add(sendMsgButton);
        add(sendMsgPanel, BorderLayout.SOUTH);
        msgField.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = msgField.getText ();
                sendMsg ( msg );
            }
        } );
        msgField.addFocusListener ( new FocusListener () {
            @Override
            public void focusGained(FocusEvent e) {
                if (msgField.getText ().equals ("Отправить сообщение"))
                    msgField.setText ( "" );
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (msgField.getText ().isEmpty ())
                    msgField.setText ( "Отправить сообщение" );
            }
        } );
        sendMsgButton.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = msgField.getText ();
                sendMsg ( msg );
            }
        } );

        clientListPanel = new JPanel ( new BorderLayout());
        countOfClientsField = new JTextArea ( "Онлайн: " );
        countOfClientsField.setEditable ( false );
        clientListArea = new JTextArea (  );
        clientListArea.setEditable ( false );
        clientListArea.setPreferredSize ( new Dimension ( 150,1 ) );
        clientListPanel.add(countOfClientsField, BorderLayout.NORTH);
        clientListPanel.add(clientListArea,BorderLayout.CENTER);
        countOfClientsField.setBackground ( new Color ( 203, 255, 244 ));
        clientListArea.setBackground ( new Color ( 203, 255, 244 ));
        add(clientListPanel, BorderLayout.EAST);

        msgHistoryArea = new JTextArea (  );
        msgHistoryArea.setEditable ( false );
        add(msgHistoryArea, BorderLayout.CENTER);
        setAuthorised ( false );

        startWindow = new JPanel(new GridLayout(1,2));
        logIn = new JButton("Зарегистрированный пользователь");
        newUser = new JButton("Новый пользователь");
        startWindow.add(logIn);
        startWindow.add(newUser);
        startWindow.setVisible(true);
        add(startWindow, BorderLayout.NORTH);

        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isNewUser=false;
                startWindow.setVisible(false);
                authPanel.setVisible(true);
            }
        });
        newUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isNewUser=false;
                startWindow.setVisible(false);
                authPanel.setVisible(true);
            }
        });
        layer = new JLayeredPane();
        startWindow.setBounds(0,0,640,70);
        authPanel.setBounds(0,0,640,70);
        layer.add(startWindow, new Integer(1));
        layer.add(authPanel, new Integer(0));
        add(layer);

        new Thread (()->{
                try {
                    while (true){
                        String inMsg = in.readUTF ();
                        if(inMsg.equals ( "/authisok" )){
                            msgHistoryArea.setText("");
                            setAuthorised ( true );
                            break;
                        }else if (!inMsg.startsWith ( "/userslist" )){
                            msgHistoryArea.append ( inMsg + "\n" );
                            msgHistoryArea.setCaretPosition (clientListArea.getDocument ().getLength () );
                        }else if (!inMsg.startsWith("/count")){
                            msgHistoryArea.append ( inMsg + "\n" );
                            msgHistoryArea.setCaretPosition (clientListArea.getDocument ().getLength () );
                        }
                    }
                    while(true) {
                        String inMsg = in.readUTF ();
                        if (inMsg.startsWith ( "/" )) {
                            if (inMsg.startsWith ( "/userslist" )) {
                                String[] users = inMsg.split ( " " );
                                clientListArea.setText ( "" );
                                for ( int i = 1 ; i < users.length ; i++ ) {
                                    clientListArea.append ( users[ i ] + "\n" );
                                }
                            }else if (inMsg.startsWith("/count")){
                                String[] count = inMsg.split(" ");
                                countOfClientsField.setText("");
                                countOfClientsField.setText("Онлайн: " + count[1]);
                            }
                        } else {
                            msgHistoryArea.append ( inMsg + "\n" );
                            msgHistoryArea.setCaretPosition ( msgHistoryArea.getDocument ().getLength () );
                        }
                    }
                } catch (IOException e) {
                    sendMsg ( "/end" );
                    setAuthorised ( false );
                    e.printStackTrace ();
                }

        }
        ).start ();

        setVisible ( true );
    }
    public void sendMsg (String msg){
        try {
            out.writeUTF ( msg );
            out.flush ();
            msgField.setText ( "" );
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
