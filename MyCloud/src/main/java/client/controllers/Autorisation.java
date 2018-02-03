package client.controllers;

import client.objects.Connection;
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
    private Parent fxmlWork;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Registration registration;
    private WorkWindowControll workWindow;

    private Stage regStage;
   // private Stage workStage;
    private Stage mainStage;
    private static String login;

    @FXML
    private void initialize(){
        connection = Connection.getInstance();
        initLoader();
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/registration.fxml"));
            fxmlReg = fxmlLoader.load();
            registration = fxmlLoader.getController();
            //в один fxmlLoader можно загрузить только один fxml файл

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toRegistration(ActionEvent actionEvent) {
        if(regStage==null){
            regStage = new Stage();
            regStage.setTitle("Регистрация");
            regStage.setMinWidth(350);
            regStage.setMinHeight(350);
            regStage.setScene(new Scene(fxmlReg));
            regStage.initModality(Modality.WINDOW_MODAL);
            regStage.initOwner(mainStage);
        }
        regStage.showAndWait();
    }
    public void toWorkWindow(ActionEvent actionEvent){
/*        if(workStage==null){
            workStage = new Stage();
            workStage.setTitle("Мое облако");
            workStage.setMinWidth(480);
            workStage.setMinHeight(720);
            workStage.setScene(new Scene(fxmlWork));
            workStage.initModality(Modality.WINDOW_MODAL);
            workStage.initOwner(mainStage);
        }
        workStage.showAndWait();*/
        mainStage.showAndWait();
    }

    public void autorisation(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty() ){
            wrongLabel.setText("Введите логин/пароль");
            return;
        }
        String authReq = connection.sendAuth(loginField.getText(), passwordField.getText());
        if(authReq.equals("")){
            login = loginField.getText();
            setAtorised(true);
            Node sourse = (Node) actionEvent.getSource();
            Stage stage = (Stage) sourse.getScene().getWindow();
            stage.hide();
        }else wrongLabel.setText(authReq);

    }
    public void setStage(Stage stage){
        this.mainStage = stage;
    }
    public void setAtorised(boolean bool){
        isAtorised = bool;
    }

    public static String getLogin() {
        return login;
    }

    public static boolean isAtorised() {
        return isAtorised;
    }
}
