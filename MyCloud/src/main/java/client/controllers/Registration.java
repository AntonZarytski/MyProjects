package client.controllers;

import client.objects.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Registration {
    private static Connection connection;
    public TextField repPassword;
    public Label repPass;
    public Label mailBusy;
    public Label loginBusy;
    public TextField password;
    public TextField mail;
    public TextField login;
    public Button sendRegBtn;

    @FXML
    private void initialize(){
        connection = Connection.getInstance();
    }

    public void registration(ActionEvent actionEvent) {
        if (login.getText().isEmpty()){
            loginBusy.setText("Введите логин");
        }else if (mail.getText().isEmpty()){
            mailBusy.setText("Введите e-mail");
        }else if(password.getText().isEmpty() && repPass.getText().isEmpty()){
            repPass.setText("Введите пароль");
        }else if(!repPassword.getText().equals(password.getText()) || password.getText().isEmpty() || repPassword.getText().isEmpty() ) {
            repPass.setText("Пароли должны совпадать");
        }else{
            try {
                String regReq = connection.sendReg(login.getText(), password.getText(), mail.getText());
                if(!regReq.equals("mailBusy")){

                    if(!regReq.equals("loginBusy")){

                        if (regReq.equals("regok"))
                            cancel(actionEvent);

                    }else loginBusy.setText("Такой логин уже используется");

                }else mailBusy.setText("Такой e-mail уже используется");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void cancel(ActionEvent actionEvent) {
        Node sourse = (Node) actionEvent.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.hide();
    }
}
