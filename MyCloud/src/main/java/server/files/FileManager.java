package server.files;

import server.interfaces.fileWorkAbility;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileManager implements fileWorkAbility {
    private final String rootPath = "../cloud/";
    private File file;
    private FileMessage fm;

    @Override
    public void createFolder(String folderName) {
        File file = new File(rootPath + folderName + "/");
        file.mkdirs();
    }

    @Override
    public void sendPath(String name, ObjectOutputStream oos) throws IOException {
        /**name - имя пользователя формирующее путь на сервере rootPath/userName/*/
        file = new File(rootPath + name + "/");
        File[] file = this.file.listFiles();
        oos.writeObject(file);
        oos.flush();
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
        //TODO доделать принятие файла

    }

    public void refreshPath(File file) {


    }
}
