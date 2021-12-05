package controllers;

import core.Block;
import core.TXInput;
import core.TXOutput;
import core.Transaction;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepository;

import java.util.*;

public class TransactionController {

    private static final int REWARD = 40;
    private final BlockRepository blockRepository;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * create a transaction without inputs - also called coinbase transaction
     *
     * @param miner address of miner who will receive a reward if a block is successfully mined
     * @param data  random scriptSig for input
     * @return new coinbase transaction
     */
    public Transaction newCoinbaseTX(String miner, String data) {
        if (data == null || data.equals("")) data = "Reward to " + miner;
        TXInput txInput = new TXInput("0", -1, data);
        TXOutput txOutput = new TXOutput(REWARD, miner);
        return new Transaction(List.of(txInput), List.of(txOutput));
    }

    public boolean canUnlockOutput(TXInput txInput, String unlockingData) {
        return txInput.getScriptSig().equals(unlockingData);
    }

    public boolean canBeUnlocked(TXOutput txOutput, String unlockingData) {
        return txOutput.getScriptPubKey().equals(unlockingData);
    }

    /**
     * find all Unspent Transaction Outputs - UTXOs
     *
     * @param address address that can unlock output
     * @return list of UTXOs
     */
    public List<TXOutput> findAllUTXOs(String address) {
        List<TXOutput> UTXOs = new ArrayList<>();
        List<Transaction> unspentTXs = findUnspentTransactions(address);
        for (Transaction tx : unspentTXs) {
            for (TXOutput out : tx.getOutputs()) {
                if (canBeUnlocked(out, address)) UTXOs.add(out);
            }
        }
        return UTXOs;
    }


    /**
     *  find spendable UTXOs for a new transaction; there's no need to find all UTXOs,
     *  only find some UTXOs with a total value more than or equal to the transaction's value.
     *
     *  @param  address address can unlock output
     *  @param  amount value spend in a transaction
     *  @return total value can spend and map store transaction id and index of spendable UTXO in that transaction
     */
    public Pair<Integer, Map<String, List<Integer>>> findSpendableUTXOs(String address, int amount) {
        Map<String, List<Integer>> spendableOutputs = new HashMap<>();
        List<Transaction> unspentTxs = findUnspentTransactions(address);
        int value = 0;

        txLoop:
        for (Transaction tx : unspentTxs) {
            for (TXOutput out : tx.getOutputs()) {
                if (canBeUnlocked(out, address)) {
                    value += out.getValue();
                    spendableOutputs
                            .computeIfAbsent(tx.getId(), k -> new ArrayList<>())
                            .add(tx.getOutputs().indexOf(out));
                    if (value >= amount) break txLoop;
                }
            }
        }

        return new ImmutablePair<>(value, spendableOutputs);
    }

    /**
     * find all transactions contain UTXO of an address
     *
     * @param address address that can unlock UTXO
     * @return list of tx contain UTXOs
     */
    public List<Transaction> findUnspentTransactions(String address) {

        Set<Transaction> unspentTXs = new HashSet<>();

        // store spent tx output
        // key is txId
        // value is array index of spent output in that tx
        Map<String, List<Integer>> spentTXOs = new HashMap<>();

        Block block = this.blockRepository.getLastBlock();

        while (block != null) {

            // iterator transactions of block
            for (Transaction tx : block.getTransactions()) {
                String txId = tx.getId();
                // iterator output of transaction
                for (TXOutput txOutput : tx.getOutputs()) {
                    // if this tx has spent output
                    if (spentTXOs.containsKey(txId)
                            && spentTXOs.get(txId).contains(tx.getOutputs().indexOf(txOutput))) continue;

                    if (canBeUnlocked(txOutput, address)) unspentTXs.add(tx);
                }

                // if not coinbase
                if (!isCoinbase(tx)) {
                    for (TXInput inp : tx.getInputs()) {
                        if (canUnlockOutput(inp, address)) {
                            spentTXOs
                                    .computeIfAbsent(inp.getTxId(), k -> new ArrayList<>())
                                    .add(inp.getOutId());
                        }
                    }
                }
            }

            block = this.blockRepository.getPreviousBlock(block);
        }

        return new ArrayList<>(unspentTXs);
    }


    /**
     * Check a transaction is coinbase
     *
     * @param tx transaction
     * @return return true if tx is coinbase, otherwise return false
     */
    public boolean isCoinbase(Transaction tx) {
        return tx.getInputs().size() == 1
                && tx.getInputs().get(0).getTxId().equals("0")
                && tx.getInputs().get(0).getOutId() == -1;
    }


    public void getBalance(String address) {
        int balance = 0;
        List<TXOutput> txOutputs = findAllUTXOs(address);
        for (TXOutput out : txOutputs) {
            balance += out.getValue();
        }
        logger.info("Balance of {} is {}", address, balance);
    }

    /**
     * transaction to send coin
     *
     * @param from   address to send coin
     * @param to     address to receive coin
     * @param amount value to send
     * @return new transaction
     */
    public Transaction newTransaction(String from, String to, int amount) {
        List<TXInput> inputs = new ArrayList<>();
        List<TXOutput> outputs = new ArrayList<>();

        // get value of coin and outputs can spend for transaction
        Pair<Integer, Map<String, List<Integer>>> pair = findSpendableUTXOs(from, amount);

        int spendValue = pair.getLeft();
        Map<String, List<Integer>> spendableOutputs = pair.getRight();

        // if not enough coin
        if (spendValue < amount) {
            logger.error("{} not enough {} coins!", from, amount);
            return null;
        }

        // build inputs of transaction
        spendableOutputs.forEach((txId, outputIds) -> {
            for (int i : outputIds) {
                TXInput input = new TXInput(txId, i, from);
                inputs.add(input);
            }
        });

        // output to send amount to receiver
        outputs.add(new TXOutput(amount, to));

        // output to send tiền thừa
        if (spendValue > amount) {
            outputs.add(new TXOutput(spendValue - amount, from));
        }

        return new Transaction(inputs, outputs);

    }

}
