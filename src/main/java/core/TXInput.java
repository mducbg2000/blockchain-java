package core;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class TXInput implements Serializable {
    private String txId;
    private int outId;
    private String scriptSig;

    public TXInput(){}

    public TXInput(String txId, int outId, String scriptSig) {
        this.txId = txId;
        this.outId = outId;
        this.scriptSig = scriptSig;
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

    public String getScriptSig() {
        return scriptSig;
    }

    public void setScriptSig(String scriptSig) {
        this.scriptSig = scriptSig;
    }


}
