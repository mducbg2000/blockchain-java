package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction implements Serializable {
    private List<TXInput> inputs;
    private List<TXOutput> outputs;
    private String id;

    public Transaction(){
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.setId();
    }

    public Transaction(List<TXInput> inputs, List<TXOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.setId();
    }

    public List<TXInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TXInput> inputs) {
        this.inputs = inputs;
    }

    public List<TXOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TXOutput> outputs) {
        this.outputs = outputs;
    }

    public void addOutputs(TXOutput output) {
        this.outputs.add(output);
    }

    public String getId() {
        return id;
    }

    private void setId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transaction) {
            return id.equals(((Transaction) o).id);
        }
        return false;
    }

}
