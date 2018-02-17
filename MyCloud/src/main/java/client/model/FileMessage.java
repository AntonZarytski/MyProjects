package client.model;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private final static long serialVersionUID = 6549841687984189L;
    private String name;
    private byte[] data;
    private String path;

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
        this.path = path;
        this.name = name;
        this.data = data;
    }
}
