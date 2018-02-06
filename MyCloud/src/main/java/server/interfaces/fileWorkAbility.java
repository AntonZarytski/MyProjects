package server.interfaces;

import server.files.FileMessage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface fileWorkAbility {
    void createFolder(String name);
    void sendPath(String name, ObjectOutputStream oos) throws IOException;
    void renameFile(File file);
    void copyFile(File file);
    void pasteFile(File file);
    void deleteFile(File File);
    void sendFile(String path, String fileName, ObjectOutputStream oos) throws IOException;
    void getFile(FileMessage fileMessage) throws IOException;
}
