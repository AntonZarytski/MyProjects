package server.interfaces;


import client.model.FileMessage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface fileWorkAbility {
    void createFolder(String name);

    void sendPath(String name, ObjectOutputStream oos) throws IOException;

    void renameFile(String oldName, String newName);

    void copyFile(File file);

    void pasteFile(File file);

    void deleteFile(File File);

    void sendFile(String serverFilePath, String clientFilePath, ObjectOutputStream oos) throws IOException;

    void getFile(FileMessage fileMessage) throws IOException;
}
