package core;


import utils.HashUtil;
import utils.CryptoUtil;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Arrays;

public class TXInput implements Serializable {
    private String txId;
    private int outId;
    private byte[] signature;
    private PublicKey publicKey;

    public TXInput(){}

    public TXInput(String txId, int outId) {
        this.txId = txId;
        this.outId = outId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public boolean useAddress(String address) {
        byte[] pubKeyHash = CryptoUtil.getPubKeyHashFromAddress(address);
        return useKey(pubKeyHash);
    }

    public boolean useKey(byte[] pubKeyHash) {
        byte[] lockingHash = HashUtil.hashPubKey(publicKey);
        return Arrays.equals(pubKeyHash, lockingHash);
    }

}
