
import java.time.Instant;

public class Block {
    private String hash;
    private final String prevHash;
    private final Instant timeStamp;
    private final String data;

    public Block(String data, String prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = Instant.now();
        this.data = data;
        this.setHash();
    }

    public void setHash() {
        String header = this.prevHash + this.timeStamp.toString() + this.data;
        this.hash = Utils.hash(header);
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }


    @Override
    public String toString() {
        return "Block{" +
                "\nprevHash=" + prevHash +
                "\nhash=" + hash +
                "\ntimeStamp=" + timeStamp +
                "\ndata=" + data +
                "\n}";
    }
}
