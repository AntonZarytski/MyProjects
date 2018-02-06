package client.controllers;

import client.files.FileManager;
import client.objects.Connection;
import javafx.collections.FXCollections;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    public ListView<Path> mainViewList;
    public Queue<String> commandsList;
    public Button refreshBtn;
    private List<Path> pathList;
    private List<String> stringList;
    private ObservableList<Path> paths;
    private Connection connection;
    private FileManager fm;
    private Stage mainStage;
    private Parent fxmlAuth;
    private Stage authStage;
    private Autorisation authWindow;
    private final String login = Autorisation.getLogin();
    private Path root;

    @FXML
    private void initialize() {
        connection = Connection.getInstance();
        fm = new FileManager();
        commandsList = new PriorityQueue<>();
        //stringList = convertToString(pathList);
        pathList = fm.getPaths();
        paths = FXCollections.observableArrayList(pathList);
        ;
        mainViewList.setItems(paths);
        initLoader();
        toAutorise();

        mainViewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    File path = mainViewList.getSelectionModel().getSelectedItem().toFile();
                    if (path.isDirectory()) {
                        root = mainViewList.getSelectionModel().getSelectedItem();
                    }
                    if (path.isDirectory()) {
                        refreshFiles(path);
                    }
                }
            }
        });
        mainViewList.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != mainViewList && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        mainViewList.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasFiles()) {
                        System.out.println(db.getFiles().get(0).getAbsolutePath());
                        fm.sendFile(root.toString(), db.getFiles().get(0).getAbsolutePath(), Connection.getObjOut());
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private List<String> convertToString(List<Path> list) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            stringList.add(list.get(i).toString());
        }
        return stringList;
    }

    private void toAutorise() {
        if (authStage == null) {
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

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    public void refreshFiles(ActionEvent actionEvent) {
        try {
            connection.getPath();
            mainViewList.setItems(paths);
            fm.getPaths().clear();
            fm.refreshPath(Connection.getObjIn());
            refreshFileList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshFileList() {
        paths.clear();
        paths.addAll(fm.getPaths());
    }

    public void directoryUp(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Path file = root;
            String path = file.getParent().toString();
            root = Paths.get(path);
            //TODO дырка, как сделать что бы выше каталога-логина нельзя было поднятся?
            String crutch = "..\\cloud";
            if (!path.equals(crutch)) {
                file = Paths.get(path);
                refreshFiles(file.toFile());
            }
        }
    }

    private void refreshFiles(File file) {
        paths.clear();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            paths.add(files[i].toPath());
        }
    }

    //TODO нужен ли класс FileManager или можно повесить на контроллер реализацию fileWorkAbility

    public void downloadOnDisk(ActionEvent actionEvent) throws IOException {
        File file = chooseFile();
        System.out.println("файл отправки " + file.toString());
        System.out.println("путь отправки " + root.toString());
        fm.sendFile(root.toString(), file.toString(), Connection.getObjOut());
    }
    private File chooseFile(){
        Desktop desktop = Desktop.getDesktop();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainStage);
        return file;
    }

    public void downloadToComp(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        File path = chooseFile();
        File file = mainViewList.getSelectionModel().getSelectedItem().toFile();
        String command = "/getfile " + file.toString();
        commandsList.add(command);
        connection.sendCommands(commandsList);
        fm.getFile(path.toString(), Connection.getObjIn());
    }

    public void renameFile(ActionEvent actionEvent) {
        File file = mainViewList.getSelectionModel().getSelectedItem().toFile();

    }

    public void copyFile(ActionEvent actionEvent) {
    }

    public void cutFile(ActionEvent actionEvent) {

    }

    public void pasteFile(ActionEvent actionEvent) {

    }

    public void deleteFile(ActionEvent actionEvent) throws IOException {
        File file = mainViewList.getSelectionModel().getSelectedItem().toFile();
        String command = "/delete" + " " + file.toString();
        commandsList.add(command);
        paths.remove(file.toPath());
        connection.sendCommands(commandsList);

    }

    public void createFolder(ActionEvent actionEvent) {
        File file = root.toFile();
        String command = "/createfolder" + " " + file.toString();

    }
}
