package client.controllers;

import client.model.FileManager;
import client.model.Connection;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
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

    public static final String AUTORISATION_TITLE = "Авторизация";
    public static final String EDIT_TITLE = "Редактирование";
    public static final String GETFILE_COMMAND = "/getfile ";
    public static final String RENAME_COMMAND = "/rename ";
    public static final String DELETE_COMMAND = "/delete";
    public static final String CREATEFOLDER_COMMAND = "/createfolder";
    public Label memoryMbLabel;
    public Label engageMbLabel;
    public Label freeMbLabel;
    public Label musicMbLabel;
    public Label videoMbLabel;
    public Label docMbLabel;
    public Label photoMbLabel;
    public Label otherMbLabel;
    public ListView<Path> mainViewList;
    public Queue<String> commandsList;
    public ContextMenu contextMenu;
    private List<Path> pathList;
    private ObservableList<Path> paths;
    private Connection connection;
    private FileManager fm;
    private Stage mainStage;
    private Parent fxmlAuth;
    private Stage authStage;
    private Autorisation authWindow;
    private Parent fxmlEdit;
    private EnterText editWindow;
    private Stage editDialogStage;
    private static String userId;
    private Path root;

    public static void setUserId(String userId) {
        WorkWindowControll.userId = userId;
    }

    @FXML
    private void initialize() throws IOException, ClassNotFoundException {
        connection = Connection.getInstance();
        fm = new FileManager();
        commandsList = new PriorityQueue<>();
        pathList = fm.getPaths();
        paths = FXCollections.observableArrayList(pathList);
        mainViewList.setItems(paths);
        initLoader();
        toAutorise();
        refreshPaths();
        initListeners();
    }
    private void initMemoryData(){
        //LongProperty memory = new LongProperty(paths.stream().mapToLong((s) -> (s.toFile().length())).sum();
        /*memoryMbLabel;
        engageMbLabel;
        freeMbLabel;
        musicMbLabel;
        videoMbLabel;
        docMbLabel;
        photoMbLabel;
        otherMbLabel;*/
    }
    private void initListeners() {
        mainViewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (mainViewList.getSelectionModel().getSelectedItem() == null) {
                        return;
                    }
                    File path = mainViewList.getSelectionModel().getSelectedItem().toFile();
                    if (path.isDirectory()) {
                        root = mainViewList.getSelectionModel().getSelectedItem();
                        refreshFiles(root.toFile());
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
                        Path downloadingFile = db.getFiles().get(0).toPath();
                        String downloadingFileName = downloadingFile.getName(downloadingFile.getNameCount() - 1).toString();
                        System.out.println(downloadingFileName);
                        fm.sendFile(root.toString() + "/" + downloadingFileName, downloadingFile.toString(), Connection.getObjOut());
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

    public void runContextMenu(ActionEvent actionEvent) {
    }

    private void toAutorise() {
        if (authStage == null) {
            authStage = new Stage();
            authStage.setTitle(AUTORISATION_TITLE);
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
            FXMLLoader fxmlAuthorisation = new FXMLLoader();
            fxmlAuthorisation.setLocation(getClass().getResource("../fxml/autorisation.fxml"));
            fxmlAuth = fxmlAuthorisation.load();
            authWindow = fxmlAuthorisation.getController();
            authWindow.setStage(mainStage);
            FXMLLoader fxmlChangeName = new FXMLLoader();
            fxmlChangeName.setLocation(getClass().getResource("../fxml/enter_text.fxml"));
            fxmlEdit = fxmlChangeName.load();
            editWindow = fxmlChangeName.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }


    private void refreshPaths() throws IOException, ClassNotFoundException {
        connection.getPath();
        fm.getPaths().clear();
        fm.refreshPath(Connection.getObjIn());
        paths.clear();
        paths.addAll(fm.getPaths());
        mainViewList.setItems(paths);
    }

    private void refreshFiles(File file) {
        paths.clear();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            paths.add(files[i].toPath());
        }
    }

    public void directoryUp(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String crutch = "..\\cloud\\" + userId;
            if (root == null) {
                root = Paths.get(crutch);
            }
            if (!root.getFileName().toString().equals(userId)) {
                root = root.getParent();
                refreshFiles(root.toFile());
            }
        }
    }

    private void showEditWindow() {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle(EDIT_TITLE);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }
        editDialogStage.showAndWait();
    }


    public void downloadOnDisk(ActionEvent actionEvent) throws IOException {
        Path file = chooseFile(false, "").toPath();
        String fileName = file.getName(file.getNameCount() - 1).toString();
        if (file != null && !file.toFile().isDirectory()) {
            fm.sendFile(root.toString() + "/" + fileName, file.toString(), Connection.getObjOut());
        }
    }

    private File chooseFile(boolean isSave, String downloadfile) {
        Desktop desktop = Desktop.getDesktop();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All model", "*.*"),
                new FileChooser.ExtensionFilter("HTML Documents", "*.html"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx"),
                new FileChooser.ExtensionFilter("XLM", "*.xlm"),
                new FileChooser.ExtensionFilter("PPT", "*.ppr"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PSD", "*.psd"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("FLAC", "*.flac"),
                new FileChooser.ExtensionFilter("WMV", "*.wmv"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"),
                new FileChooser.ExtensionFilter("MKV", "*.mcv")
        );
        fileChooser.setInitialFileName(downloadfile);
        File file = null;
        if (isSave) {
            file = fileChooser.showSaveDialog(mainStage);
        } else {
            file = fileChooser.showOpenDialog(mainStage);
        }
        return file;
    }

    public void downloadToComp(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Path serverFilePath = mainViewList.getSelectionModel().getSelectedItem();
        String downloadingFileName = serverFilePath.getName(serverFilePath.getNameCount() - 1).toString();
        File clientFilePath = chooseFile(true, downloadingFileName);
        if (serverFilePath != null && clientFilePath != null) {
            String command = GETFILE_COMMAND + serverFilePath.toString() + " " + clientFilePath;
            commandsList.add(command);
            connection.sendCommands(commandsList);
            commandsList.clear();
            fm.getFile(clientFilePath.toString(), serverFilePath.toString(), Connection.getObjIn());
        }
    }

    //TODO метод срабатывает только один раз. Почему??
    public void renameFile(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Path file = mainViewList.getSelectionModel().getSelectedItem();
        showEditWindow();
        paths.remove(file);
        paths.add(new File(root.toString() + "\\" + editWindow.getEditableData()).toPath());
        String command = RENAME_COMMAND + file.toString() + " " + root + "\\" + editWindow.getEditableData();
        commandsList.add(command);
        connection.sendCommands(commandsList);
    }

    public void copyFile(ActionEvent actionEvent) {
    }

    public void cutFile(ActionEvent actionEvent) {

    }

    public void pasteFile(ActionEvent actionEvent) {

    }

    public void deleteFile(ActionEvent actionEvent) throws IOException {
        File file = mainViewList.getSelectionModel().getSelectedItem().toFile();
        if (file != null) {
            String command = DELETE_COMMAND + " " + file.toString();
            commandsList.add(command);
            paths.remove(file.toPath());
            connection.sendCommands(commandsList);
            commandsList.clear();
        }
    }

    public void createFolder(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        showEditWindow();
        String path;
        if (root == null)
            path = "..\\cloud\\" + userId;
        else path = root.toString();
        String command = CREATEFOLDER_COMMAND + " " + path + "\\" + editWindow.getEditableData();
        commandsList.add(command);
        connection.sendCommands(commandsList);
        File file = new File(path + "\\" + editWindow.getEditableData());
        paths.add(file.toPath());
        refreshPaths();
        refreshFiles(root.toFile());
    }
}
