package core;

import utils.CryptoUtil;

import java.io.Serializable;
import java.util.Arrays;

public class TXOutput implements Serializable {
    private int value;
    private byte[] pubKeyHash;

    public TXOutput() {}

    public TXOutput(int value, String address) {
        this.value = value;
        this.lock(address);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public byte[] getPubKeyHash() {
        return pubKeyHash;
    }

    public void setPubKeyHash(byte[] pubKeyHash) {
        this.pubKeyHash = pubKeyHash;
    }

    public boolean isLockedWith(String address) {
        byte[] pubKeyHash = CryptoUtil.getPubKeyHashFromAddress(address);
        return isLockedWith(pubKeyHash);
    }

    public boolean isLockedWith(byte[] pubKeyHash) {
        return Arrays.equals(pubKeyHash, this.pubKeyHash);
    }

    public void lock(String address) {
        this.pubKeyHash = CryptoUtil.getPubKeyHashFromAddress(address);
    }
}
