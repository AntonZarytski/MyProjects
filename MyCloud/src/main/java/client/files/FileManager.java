package client.files;


import client.Interfaces.fileWorkAbility;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileManager implements fileWorkAbility {
    private final String rootPath = "../cloud/";
    private File file = new File(rootPath);
    private FileMessage fm;
    private List<File> files;

    public FileManager() {
        this.files = new ArrayList<>();
    }

    @Override
    public void createFolder(String folderName) {

    }

    @Override
    public void renameFile(File file) {

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
    public void sendFile(String fileName, ObjectOutputStream oos) throws IOException {
        fm = new FileMessage(fileName, Files.readAllBytes(Paths.get(fileName)));
        oos.writeObject(fm);
        oos.flush();
    }

    @Override
    public void getFile(String filePath, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        fm = (FileMessage) ois.readObject();
        //TODO было files.get(1).getPath()
        Files.write(Paths.get(files.get(1) + fm.getName()), fm.getData(), StandardOpenOption.CREATE);

    }

    public void refreshPath(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        File[] pathes = (File[]) ois.readObject();
        files.addAll(Arrays.asList(pathes));
    }

    public List<File> getFiles() {
        return files;
    }

    public void refreshFiles() {

    }
}
