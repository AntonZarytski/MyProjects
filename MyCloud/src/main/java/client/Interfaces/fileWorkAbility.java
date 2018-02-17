package client.Interfaces;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface fileWorkAbility {
    void createFolder(String name);

    void renameFile(String oldName, String newName);

    void copyFile(File file);

    void pasteFile(File file);

    void deleteFile(File file);

    void sendFile(String path, String fileName, ObjectOutputStream oos) throws IOException;

    void getFile(String fileSavePath, String fileServerPath, ObjectInputStream ois) throws IOException, ClassNotFoundException;
}
