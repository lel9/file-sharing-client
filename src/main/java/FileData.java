import java.util.UUID;

public class FileData {
    private String name;
    private String sender;
    private UUID id;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender() {
        return sender;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public FileData(UUID id, String name, String sender, long size) {
        this.id = id;
        this.name = name;
        this.sender = sender;
        this.size = size;
    }
}
