package client.files;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private final static long serialVersionUID = 6549841687984189L;
    private String name;
    private String path;
    private byte[] data;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(String path, String name, byte[] data) {
        this.name = name;
        this.path = path;
        this.data = data;
    }
}
