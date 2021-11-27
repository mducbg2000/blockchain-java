package core;

import java.io.Serializable;
import java.time.Instant;

public class Block implements Serializable {
    private String hash;
    private final String prevHash;
    private final Instant timeStamp;
    private final String data;
    private int nonce;

    public Block(String data, String prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = Instant.now();
        this.data = data;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "Block{" +
                "\nprevHash=" + prevHash +
                "\nhash=" + hash +
                "\ntimeStamp=" + timeStamp +
                "\ndata=" + data +
                "\nnonce=" + nonce +
                "\n}";
    }
}
