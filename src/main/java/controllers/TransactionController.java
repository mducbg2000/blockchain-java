package controllers;

import core.Block;
import core.TXInput;
import core.TXOutput;
import core.Transaction;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepository;

import java.security.PrivateKey;
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
     * @return new coinbase transaction
     */
    public Transaction newCoinbaseTX(String miner) {
        TXInput txInput = new TXInput("0", -1);
        TXOutput txOutput = new TXOutput(REWARD, miner);
        return new Transaction(List.of(txInput), List.of(txOutput));
    }

    public byte[] signTransaction(PrivateKey privateKey, Transaction tx, List<Transaction> prevTxs) {

        if (isCoinbase(tx)) return null;

        return new byte[]{};
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
                if (out.isLockedWith(address)) UTXOs.add(out);
            }
        }
        return UTXOs;
    }


    /**
     * find spendable UTXOs for a new transaction; there's no need to find all UTXOs,
     * only find some UTXOs with a total value more than or equal to the transaction's value.
     *
     * @param address address can unlock output
     * @param amount  value spend in a transaction
     * @return total value can spend and map store transaction id and index of spendable UTXO in that transaction
     */
    public Pair<Integer, Map<String, Integer>> findSpendableUTXOs(String address, int amount) {
        Map<String, Integer> spendableOutputs = new HashMap<>();
        List<Transaction> unspentTxs = findUnspentTransactions(address);
        int value = 0;

        txLoop:
        for (Transaction tx : unspentTxs) {
            for (TXOutput out : tx.getOutputs()) {
                if (out.isLockedWith(address)) {
                    value += out.getValue();
                    spendableOutputs.put(tx.getId(), tx.getOutputs().indexOf(out));
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
        Set<String> spentTXs = new HashSet<>();

        Block block = this.blockRepository.getLastBlock();

        while (block != null) {

            for (Transaction tx : block.getTransactions()) {
                String txId = tx.getId();

                for (TXOutput txOutput : tx.getOutputs()) {
                    if (spentTXs.contains(txId)) continue;
                    if (txOutput.isLockedWith(address)) unspentTXs.add(tx);
                }

                if (!isCoinbase(tx)) {
                    for (TXInput inp : tx.getInputs()) {
                        if (inp.useAddress(address)) {
                            spentTXs.add(inp.getTxId());
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

        Pair<Integer, Map<String, Integer>> pair = findSpendableUTXOs(from, amount);

        int spendValue = pair.getLeft();
        Map<String, Integer> spendableOutputs = pair.getRight();

        if (spendValue < amount) {
            logger.error("{} not enough {} coins!", from, amount);
            return null;
        }

        // build inputs of transaction

        spendableOutputs.forEach((txId, outputId) -> {
            TXInput input = new TXInput(txId, outputId);
            inputs.add(input);
        });

        // output to send amount to receiver
        outputs.add(new TXOutput(amount, to));

        // output to send change
        if (spendValue > amount) {
            outputs.add(new TXOutput(spendValue - amount, from));
        }

        return new Transaction(inputs, outputs);

    }



}
