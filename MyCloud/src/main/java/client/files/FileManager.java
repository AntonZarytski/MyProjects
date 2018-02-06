package client.files;


import client.Interfaces.fileWorkAbility;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class FileManager implements fileWorkAbility {
    private final String rootPath = "../cloud/";
    private File file = new File(rootPath);
    private FileMessage fm;
    private List<File> files;
    private List<Path> paths;

    public FileManager() {
        this.files = new ArrayList<>();
        this.paths = new ArrayList<>();
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
        //TODO отпилить
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
        this.fm = new FileMessage(path, fileName, Files.readAllBytes(Paths.get(fileName)));
        System.out.println("отправлен файл по имени " + fm.getName() + " по пути " + fm.getPath());
        System.out.println("отправлен файл по имени " + fm.getName() + " по пути " + fm.getPath());
        oos.writeObject(fm);
        oos.flush();
    }

    @Override
    public void getFile(String filePath, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        this.fm = (FileMessage) ois.readObject();
        //TODO было files.get(1).getPath()
        Files.write(Paths.get(files.get(1) + fm.getName()), fm.getData(), StandardOpenOption.CREATE);

    }

    public void refreshPath(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        File[] pathes = (File[]) ois.readObject();
        paths.clear();
        for (int i = 0; i <pathes.length ; i++) {
            paths.add(pathes[i].toPath());
        }

    }
    public List<Path> getPaths() {
        return paths;
    }

    public void refreshFiles() {

    }
}
