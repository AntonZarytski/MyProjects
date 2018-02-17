package client.controllers;

import client.model.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Autorisation {
    public static final String FXML_REGISTRATION_FXML = "../fxml/registration.fxml";
    public static final String REGISTRATION = "Регистрация";
    public static final String ENTER_LOGIN_PASSWORD = "Введите логин/пароль";
    private Connection connection;
    private static boolean isAtorised = false;
    @FXML
    Label wrongLabel;
    @FXML
    TextField passwordField;
    @FXML
    TextField loginField;
    @FXML
    VBox authBox;
    private Parent fxmlReg;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Registration registration;
    private Stage regStage;
    private Stage mainStage;

    @FXML
    private void initialize() {
        connection = Connection.getInstance();
        initLoader();
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource(FXML_REGISTRATION_FXML));
            fxmlReg = fxmlLoader.load();
            registration = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toRegistration(ActionEvent actionEvent) {
        if (regStage == null) {
            regStage = new Stage();
            regStage.setTitle(REGISTRATION);
            regStage.setMinWidth(350);
            regStage.setMinHeight(350);
            regStage.setScene(new Scene(fxmlReg));
            regStage.initModality(Modality.WINDOW_MODAL);
            regStage.initOwner(mainStage);
        }
        regStage.showAndWait();
    }

    public void toWorkWindow(ActionEvent actionEvent) {
        mainStage.showAndWait();
    }

    public void autorisation(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            wrongLabel.setText(ENTER_LOGIN_PASSWORD);
            return;
        }
        String login = String.valueOf(this.loginField.getText().hashCode());
        String password = String.valueOf(this.passwordField.getText().hashCode());
        System.out.println("login " + login + " password " + password);
        String authReq = connection.sendAuth(login, password);
        if (authReq.equals("")) {
            setAtorised(true);
            Node sourse = (Node) actionEvent.getSource();
            Stage stage = (Stage) sourse.getScene().getWindow();
            stage.hide();
        } else wrongLabel.setText(authReq);

    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setAtorised(boolean bool) {
        isAtorised = bool;
    }

    public static boolean isAtorised() {
        return isAtorised;
    }
}
