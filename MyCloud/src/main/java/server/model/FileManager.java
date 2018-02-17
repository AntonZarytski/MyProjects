package server.model;

import client.model.FileMessage;
import server.interfaces.fileWorkAbility;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class FileManager implements fileWorkAbility {
    private final String rootPath = "../cloud/";
    private File file;

    @Override
    public void createFolder(String folderName) {
        File file = new File(folderName + "/");
        file.mkdirs();
    }

    @Override
    public void sendPath(String name, ObjectOutputStream oos) throws IOException {
        file = new File(rootPath + name + "/");
        File[] files = file.listFiles();
        oos.writeObject(files);
        oos.flush();
    }

    @Override
    public void renameFile(String oldName, String newName) {
        File file = new File(oldName);
        File newFile = new File(newName);
        file.renameTo(newFile);
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
    public void sendFile(String serverFilePath, String clientFilePath, ObjectOutputStream oos) throws IOException {
        FileMessage fm = new FileMessage(serverFilePath, clientFilePath, Files.readAllBytes(Paths.get(serverFilePath)));
        oos.writeObject(fm);
        oos.flush();
    }

    @Override
    public void getFile(FileMessage fm) throws IOException {
        Files.write(Paths.get(fm.getPath()), fm.getData(), StandardOpenOption.CREATE);

    }
}
