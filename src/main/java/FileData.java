import java.util.UUID;

public class FileData {
    private String name;
    private String sender;
    private UUID id;

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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public FileData(UUID id, String name, String sender) {
        this.id = id;
        this.name = name;
        this.sender = sender;
    }
}
