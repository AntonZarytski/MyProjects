package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
    private boolean isAuthorised;

    public void setAuthorised(boolean authorised) {
        isAuthorised = authorised;
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
        authPanel = new JPanel ( new GridLayout ( 1,4 ) );
        loginField = new JTextField ( "Логин" );
        passwordField = new JPasswordField ( "Пароль" );
        nickNameField = new JTextField ( "Имя" );
        sendAuthButton = new JButton ( "Войти в чат" );
        authPanel.add(loginField);
        authPanel.add(passwordField);
        authPanel.add(nickNameField);
        authPanel.add(sendAuthButton);
        add(authPanel, BorderLayout.NORTH);
        sendAuthButton.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg ("/auth" + " " + loginField.getText () + " " + passwordField.getText () + " " + nickNameField.getText ());
                loginField.setText ( "" );
                passwordField.setText ( "" );
                nickNameField.setText ( "" );
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

        clientListPanel = new JPanel ( new GridLayout ( 2,1 ));
        countOfClientsField = new JTextArea ( "Онлайн: " );
        countOfClientsField.setEditable ( false );
        clientListArea = new JTextArea (  );
        clientListArea.setEditable ( false );
        clientListPanel.add(countOfClientsField);
        clientListPanel.add(clientListArea);
        countOfClientsField.setBackground ( new Color ( 203, 255, 244 ));
        clientListArea.setBackground ( new Color ( 203, 255, 244 ));
        add(clientListPanel, BorderLayout.EAST);

        msgHistoryArea = new JTextArea (  );
        msgHistoryArea.setEditable ( false );
        add(msgHistoryArea, BorderLayout.CENTER);
        setAuthorised ( false );
        new Thread (()->{
                try {
                    String inMsg;
                    while (true){
                        inMsg = in.readUTF ();
                        System.out.println (inMsg);
                        if(inMsg.equals ( "/authisok" )){
                            setAuthorised ( true );
                            break;
                        }
                    }
                    while(true) {
                        inMsg = in.readUTF ();
                        msgHistoryArea.append ( inMsg + "\n" );
                        msgHistoryArea.setCaretPosition (msgHistoryArea.getDocument ().getLength () );
                    }
                } catch (IOException e) {
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
