package core;

import com.google.gson.Gson;
import utils.GsonUtil;
import utils.HashUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Block implements Serializable {
    private byte[] hash;
    private final byte[] prevHash;
    private final Instant timeStamp;
    private final List<Transaction> transactions;
    private int nonce;

    public Block(List<Transaction> transactions, final byte[] prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = Instant.now();
        this.transactions = transactions;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getHashTransactions() {
        StringBuilder txHash = new StringBuilder();
        for (Transaction tx : transactions) {
            txHash.append(tx.getId());
        }
        return HashUtil.sha256ToHex(txHash.toString());
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getPrevHash() {
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
        Gson gson = GsonUtil.getGson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Block) {
            return Arrays.equals(this.hash, ((Block) o).hash);
        }
        return false;
    }

}
