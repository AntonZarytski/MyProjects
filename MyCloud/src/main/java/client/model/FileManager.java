package client.model;


import client.Interfaces.fileWorkAbility;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileManager implements fileWorkAbility {
    private final String ROOTPATH = "../cloud/";
    private List<Path> paths;

    public FileManager() {
        this.paths = new ArrayList<>();
    }

    @Override
    public void createFolder(String folderName) {

    }

    @Override
    public void renameFile(String oldName, String newName) {

    }


    @Override
    public void copyFile(File file) {

    }

    @Override
    public void pasteFile(File file) {

    }

    @Override
    public void deleteFile(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                deleteFile(f);
            file.delete();
        } else {
            file.delete();
        }
    }


    @Override
    public void sendFile(String path, String fileName, ObjectOutputStream oos) throws IOException {
        FileMessage fm = new FileMessage(path, fileName, Files.readAllBytes(Paths.get(fileName)));
        oos.writeObject(fm);
        oos.flush();
    }

    @Override
    public void getFile(String fileSavePath, String fileServerPath, ObjectInputStream ois, ProgressBar bar) throws IOException, ClassNotFoundException {
        FileMessage fm = (FileMessage) ois.readObject();
        Files.write(Paths.get(fileSavePath), fm.getData(), StandardOpenOption.CREATE);
    }

    public void refreshPath(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        File[] pathes = (File[]) ois.readObject();
        paths.clear();
        for (int i = 0; i<pathes.length; i++) {
            paths.add(pathes[i].toPath());
        }
    }

    public List<Path> getPaths() {
        return paths;
    }
    public void setPaths(List<Path> list){
        this.paths = list;
    }
}
