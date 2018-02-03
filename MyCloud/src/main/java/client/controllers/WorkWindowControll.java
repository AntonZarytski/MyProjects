package client.controllers;

import client.files.FileManager;
import client.objects.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WorkWindowControll {

    public Label memoryMbLabel;
    public Label engageMbLabel;
    public Label freeMbLabel;
    public Label musicMbLabel;
    public Label videoMbLabel;
    public Label docMbLabel;
    public Label photoMbLabel;
    public Label otherMbLabel;
    public ListView delitedViewList;
    public ListView<File> mainViewList;
    public List<String> commandsList;
    public Button refreshBtn;
    //TODO как реализовать отображение не путей файлов а имена, но что бы сохранить возможность работать как с классом File
    //TODO можно ли работать с листом файлов передавать его на сервер и что бы сервер в свою очередь осуществлял изменение файлов(тип как пакетный запрос
    //TODO и тогда в каком случае необходимо отсылать лист на сервер)
    private List<File> fileList;
    private ObservableList<File> files;
    private Connection connection;
    private FileManager fm;
    private Stage mainStage;
    private Parent fxmlAuth;
    private Stage authStage;
    private Autorisation authWindow;
    private final String login = Autorisation.getLogin();

    @FXML
    private void initialize(){
        connection = Connection.getInstance();
        fm = new FileManager();
        commandsList = new ArrayList<>();
        fileList = fm.getFiles();
        files = FXCollections.observableArrayList(fileList);
        mainViewList.setItems(files);
        initLoader();
        toAutorise();
        files.addListener(new ListChangeListener<File>() {
            @Override
            public void onChanged(Change<? extends File> c) {

            }
        });
        mainViewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2) {
                    File file = mainViewList.getSelectionModel().getSelectedItem();
                    if (file.isDirectory()) {
                        refreshFiles(file);
                    }
                }
            }
        });
    }

    private void refreshFileList(){
        files.clear();
        files.addAll(fm.getFiles());
    }
    private void toAutorise(){
        if(authStage==null){
            authStage = new Stage();
            authStage.setTitle("Авторизация");
            authStage.setMinWidth(200);
            authStage.setMinHeight(200);
            authStage.setScene(new Scene(fxmlAuth));
            authStage.initModality(Modality.WINDOW_MODAL);
            authStage.initOwner(mainStage);
        }
        authStage.showAndWait();
    }

    private void initLoader() {
        try {
            //в один fxmlLoader можно загрузить только один fxml файл
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/autorisation.fxml"));
            fxmlAuth = fxmlLoader.load();
            authWindow = fxmlLoader.getController();
            authWindow.setStage(mainStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage){
        this.mainStage = stage;
    }

    public void refreshFiles(ActionEvent actionEvent) {
            try {
                connection.getPath();
                mainViewList.setItems(files);
                fm.getFiles().clear();
                fm.refreshPath(Connection.getObjIn());
                refreshFileList();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public void directoryUp(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2) {
            File file = files.get(0);
            String path = file.toPath().getParent().getParent().toString();
            //TODO дырка, как сделать что бы выше каталога-логина нельзя было поднятся?
            String crutch = "..\\cloud";
            if(!path.equals(crutch)){
                file = new File(path);
                refreshFiles(file);
            }
        }
    }
    private void refreshFiles(File file){
        files.clear();
        files.addAll(file.listFiles());
    }

    //TODO нужен ли класс FileManager или можно повесить на контроллер реализацию fileWorkAbility

    public void downloadOnDisk(ActionEvent actionEvent) {
    }

    public void downloadToComp(ActionEvent actionEvent) {
    }

    public void renameFile(ActionEvent actionEvent) {
    }

    public void copyFile(ActionEvent actionEvent) {
    }

    public void cutFile(ActionEvent actionEvent) {
    }

    public void pasteFile(ActionEvent actionEvent) {
    }

    public void deleteFile(ActionEvent actionEvent) {
        File file = mainViewList.getSelectionModel().getSelectedItem();
        fm.deleteFile(file);
    }

    public void createFolder(ActionEvent actionEvent) {
    }
}
