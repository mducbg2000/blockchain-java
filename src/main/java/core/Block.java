package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class Block implements Serializable {
    private String hash;
    private final String prevHash;
    private final Instant timeStamp;
    private final List<Transaction> transactions;
    private int nonce;

    public Block(List<Transaction> transactions, String prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = Instant.now();
        this.transactions = transactions;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getHashTransactions() {
        String txHash = "";
        for (Transaction tx : transactions) {
            txHash = txHash.concat(tx.getId());
        }
        return DigestUtils.sha256Hex(txHash);
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Block) {
            return this.hash.equals(((Block) o).hash);
        }
        return false;
    }

}
