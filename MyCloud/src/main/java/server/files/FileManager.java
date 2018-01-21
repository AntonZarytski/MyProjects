package server.files;

import server.interfaces.fileWorkAbility;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Vector;

public class FileManager implements fileWorkAbility {
    private final String rootPath = "../cloud/";
    private File file = new File(rootPath);
    private List<File> files = new Vector<>();
    private FileMessage fm;

    @Override
    public void createFolder(String folderName) {
        File file = new File(rootPath + folderName + "/");
        files.add(file);
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
    public void deliteFile(String fileName) {

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
        Files.write(Paths.get(files.get(1).getPath() + fm.getName()), fm.getData(), StandardOpenOption.CREATE);

    }

    public void refreshPath(File file) {


    }
}
