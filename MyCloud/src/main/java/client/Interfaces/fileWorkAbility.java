package client.Interfaces;

import java.io.File;

public interface fileWorkAbility {
    void createFolder(String name);
    void renameFile(File file);
    void copyFile(File file);
    void pasteFile(File file);
    void deliteFile(File file);
    void sendFile(File file);
    void getFile(File file);
}
