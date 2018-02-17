package client.controllers;

import client.model.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Registration {
    private static final String ENTER_LOGIN = "Введите логин";
    private static final String ENTER_EMAIL = "Введите e-mail";
    private static final String ENTER_PASSWORD = "Введите пароль";
    private static final String PASSWORD_MUST_BE_EQUALS = "Пароли должны совпадать";
    private static final String MAIL_BUSY_COMMAND = "mailBusy";
    private static final String LOGIN_BUSY_COMMAND = "loginBusy";
    private static final String REGOK_COMMAND = "regok";
    private static final String THIS_LOGIN_ALREADY_USE = "Такой логин уже используется";
    private static final String THIS_EMAIL_ALREADY_USE = "Такой e-mail уже используется";
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
    private void initialize() {
        connection = Connection.getInstance();
    }

    public void registration(ActionEvent actionEvent) {
        if (login.getText().isEmpty()) {
            loginBusy.setText(ENTER_LOGIN);
        } else if (mail.getText().isEmpty()) {
            mailBusy.setText(ENTER_EMAIL);
        } else if (password.getText().isEmpty() && repPass.getText().isEmpty()) {
            repPass.setText(ENTER_PASSWORD);
        } else if (!repPassword.getText().equals(password.getText()) || password.getText().isEmpty() || repPassword.getText().isEmpty()) {
            repPass.setText(PASSWORD_MUST_BE_EQUALS);
        } else {
            try {
                String login = String.valueOf(this.login.getText().hashCode());
                String password = String.valueOf(this.password.getText().hashCode());
                String regReq = connection.sendReg(login, password, mail.getText());
                if (!regReq.equals(MAIL_BUSY_COMMAND)) {
                    if (!regReq.equals(LOGIN_BUSY_COMMAND)) {
                        if (regReq.equals(REGOK_COMMAND))
                            cancel(actionEvent);
                    } else loginBusy.setText(THIS_LOGIN_ALREADY_USE);
                } else mailBusy.setText(THIS_EMAIL_ALREADY_USE);
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
