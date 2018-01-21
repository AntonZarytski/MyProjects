package client.controllers;

import client.files.FileManager;
import client.objects.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;

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
    private ObservableList<File> files;
    private Connection connection;
    private FileManager fm;

    @FXML
    private void initialize(){
        connection = Connection.getInstance();
        mainViewList.setItems(files);
        fm = new FileManager();
        files = FXCollections.observableArrayList(fm.getFiles());
        mainViewList.setItems(files);
    }

}
