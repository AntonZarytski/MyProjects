package client.controllers;

import client.model.FileManager;
import client.model.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    public ProgressBar downloadProgress;
    public ImageView bacakIV;
    public ImageView refreshIV;
    public ImageView downloadIV;
    public ImageView uploadIV;
    public ImageView newFolderIV;
    public ImageView deleteIV;
    public ImageView copyIV;
    public ImageView cutIV;
    public ImageView renameIV;
    public ImageView pasteIV;
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
        //pathList = fm.getPaths();
        paths = FXCollections.observableArrayList(fm.getPaths());
        mainViewList.setItems(paths);
        initLoader();
        toAutorise();
        refreshPaths();
        initListeners();
        initMemoryData();
        bacakIV.setImage(new Image(new File("file:../../../resources/back.png").toURI().toString()));
        refreshIV.setImage(new Image("file:../../../resources/refresh.png"));
        downloadIV.setImage(new Image("file:../../../resources/download.png"));
        uploadIV.setImage(new Image( "file:../../../resources/upload.png"));
        newFolderIV.setImage(new Image("file:../../../resources/newFolder.png"));
        deleteIV.setImage(new Image("file:../../../resources/delete.png"));
        copyIV.setImage(new Image("file:../../../resources/copy.png"));
        cutIV.setImage(new Image("file:../../../resources/cut.png"));
        renameIV.setImage(new Image("file:../../../resources/rename.png"));
        pasteIV.setImage(new Image("file:../../../resources/paste.png"));

    }

    private void initMemoryData() throws IOException {
        String root = "..\\cloud\\" + userId;
        double MB = Math.pow(1024, 2);
        long allUseMemorySize = 16000;
        long engageSize = 0;
        long freeSize = 0;
        long videoSize = 0;
        long musicSize = 0;
        long docSize = 0;
        long imageSize = 0;
        long otherSize = 0;
        final long[] memorySize = new long[5];
        Files.walkFileTree(Paths.get(root), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".mp4") || file.toString().endsWith(".avi") || file.toString().endsWith(".mkv") || file.toString().endsWith(".wmv")) {
                    memorySize[0] += file.toFile().length();
                } else if (file.toString().endsWith(".mp3") || file.toString().endsWith(".flac") || file.toString().endsWith(".wma")) {
                    memorySize[1] += file.toFile().length();
                } else if (file.toString().endsWith(".html") || file.toString().endsWith(".txt") || file.toString().endsWith(".docx") || file.toString().endsWith(".xlm") || file.toString().endsWith(".ppt") || file.toString().endsWith(".pdf")) {
                    memorySize[2] += file.toFile().length();
                } else if (file.toString().endsWith(".jpg") || file.toString().endsWith(".psd") || file.toString().endsWith(".png") || file.toString().endsWith(".bmp")) {
                    memorySize[3] += file.toFile().length();
                } else {
                    memorySize[4] += file.toFile().length();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        engageSize = (long) (Arrays.stream(memorySize).sum() / MB);
        freeSize = allUseMemorySize - engageSize;
        videoSize = (long) (memorySize[0] / MB);
        musicSize = (long) (memorySize[1] / MB);
        docSize = (long) (memorySize[2] / MB);
        imageSize = (long) (memorySize[3] / MB);
        otherSize = (long) (memorySize[4] / MB);
        memoryMbLabel.setText(String.valueOf(allUseMemorySize));
        engageMbLabel.setText(String.valueOf(engageSize));
        freeMbLabel.setText(String.valueOf(freeSize));
        musicMbLabel.setText(String.valueOf(musicSize));
        videoMbLabel.setText(String.valueOf(videoSize));
        docMbLabel.setText(String.valueOf(docSize));
        photoMbLabel.setText(String.valueOf(imageSize));
        otherMbLabel.setText(String.valueOf(otherSize));
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
                        refreshFiles(root.toFile());
                    }
                    event.setDropCompleted(success);
                    event.consume();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mainViewList.setCellFactory(new Callback<ListView<Path>, ListCell<Path>>() {
            @Override
            public ListCell<Path> call(ListView<Path> param) {
                return new ListCell<Path>() {
                    @Override
                    protected void updateItem(Path item, boolean empty) {
                        super.updateItem(item, empty);
//                        try {
                        if (!empty) {
//                                File file;
//                                Image image;
                            Rectangle rect = new Rectangle(1, 1, 6, 6);
                            if (item.toFile().isFile()) {
                                if (item.toString().endsWith(".mp4") || item.toString().endsWith(".avi") || item.toString().endsWith(".mkv") || item.toString().endsWith(".wmv")) {
//                                        file = new File("C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\video.png");
//                                        image = new Image(file.toURI().toURL().toString());
                                    rect.setFill(Color.BLUE);
                                    setGraphic(rect);
                                } else if (item.toString().endsWith(".mp3") || item.toString().endsWith(".flac") || item.toString().endsWith(".wma")) {
//                                        file = new File("C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\music.png");
//                                        image = new Image(file.toURI().toURL().toString());
                                    rect.setFill(Color.GREEN);
                                    setGraphic(rect);
                                } else if (item.toString().endsWith(".txt") || item.toString().endsWith(".html") || item.toString().endsWith(".docx") || item.toString().endsWith(".xlm") || item.toString().endsWith(".ppt") || item.toString().endsWith(".pdf")) {
//                                        file = new File("file:.C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\doc.png");
//                                        image = new Image(file.toURI().toURL().toString());
                                    rect.setFill(Color.GREY);
                                    setGraphic(rect);
                                } else if (item.toString().endsWith(".jpg") || item.toString().endsWith(".psd") || item.toString().endsWith(".png") || item.toString().endsWith(".bmp")) {
//                                        file = new File("file:.C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\image.png");
//                                        image = new Image(file.toURI().toURL().toString());
                                    rect.setFill(Color.RED);
                                    setGraphic(rect);
                                } else {
                                    rect.setFill(Color.BLUEVIOLET);
                                    setGraphic(rect);
//                                        file = new File("file:.C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\file.png");
//                                        image = new Image(file.toURI().toURL().toString());
                                }
                            } else {
//                                    file = new File("file:.C:\\Users\\Anton&&Natasha\\Documents\\MyProjects\\MyCloud\\src\\main\\resources\\folder.png");
//                                    image = new Image(file.toURI().toURL().toString());
                                rect.setFill(Color.GOLD);
                                setGraphic(rect);
                            }
//                                ImageView imageView = new ImageView(image);
                            setText(String.format("%5s      %d bytes", item.getFileName(), item.toFile().length()));
                            setStyle("-fx-foreground: #f00;");
                        } else {
                            setText("");
                            setGraphic(null);
                        }
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
                    }
                };
            }
        });
    }

    private void changeScale(ImageView imageView, double scale) {
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
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
        fm.setPaths(new ArrayList<Path>());
        fm.refreshPath(Connection.getObjIn());
        paths.clear();
        paths.addAll(fm.getPaths());
        mainViewList.setItems(paths);
        initMemoryData();
    }

    private void refreshFiles(File file) {
        paths.clear();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            paths.add(files[i].toPath());
        }
        //mainViewList.setItems(paths);
    }

    public void directoryUp(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String rootstr = "..\\cloud\\" + userId;
            if (root == null) {
                root = Paths.get(rootstr);
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
        refreshFiles(root.toFile());
    }

    private File chooseFile(boolean isSave, String downloadfile) {
        Desktop desktop = Desktop.getDesktop();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All model", "*.*"),
                new FileChooser.ExtensionFilter("HTML Documents", "*.html"),
                new FileChooser.ExtensionFilter("TXT Documents", "*.txt"),
                new FileChooser.ExtensionFilter("DOCX Documents", "*.docx"),
                new FileChooser.ExtensionFilter("XLM Documents", "*.xlm"),
                new FileChooser.ExtensionFilter("PPT Documents", "*.ppt"),
                new FileChooser.ExtensionFilter("PDF Documents", "*.pdf"),
                new FileChooser.ExtensionFilter("JPG Documents", "*.jpg"),
                new FileChooser.ExtensionFilter("PSD Documents", "*.psd"),
                new FileChooser.ExtensionFilter("PNG Documents", "*.png"),
                new FileChooser.ExtensionFilter("MP3 Documents", "*.mp3"),
                new FileChooser.ExtensionFilter("FLAC Documents", "*.flac"),
                new FileChooser.ExtensionFilter("WMV Documents", "*.wmv"),
                new FileChooser.ExtensionFilter("MP4 Documents", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI Documents", "*.avi"),
                new FileChooser.ExtensionFilter("MKV Documents", "*.mkv")
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
            fm.getFile(clientFilePath.toString(), serverFilePath.toString(), Connection.getObjIn(), downloadProgress);
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

    public void refreshViewList(ActionEvent actionEvent) {
        refreshFiles(root.toFile());
    }
}
