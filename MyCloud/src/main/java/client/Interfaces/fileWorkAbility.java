package client.Interfaces;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface fileWorkAbility {
    void createFolder(String name);
    void renameFile(File file);
    void copyFile(File file);
    void pasteFile(File file);
    void deleteFile(File file);
    void sendFile(String fileName, ObjectOutputStream oos) throws IOException;
    void getFile(String filePath, ObjectInputStream ois) throws IOException, ClassNotFoundException;
}
