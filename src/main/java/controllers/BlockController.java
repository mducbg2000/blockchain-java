package controllers;

import core.Block;
import core.Transaction;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepository;
import utils.HashUtil;

import java.util.Arrays;
import java.util.List;

public class BlockController {
    private final BlockRepository blockRepository;
    private final TransactionController txController;
    private final static Logger logger = LoggerFactory.getLogger(BlockController.class);

    public BlockController(BlockRepository blockRepository, TransactionController txController) {
        this.blockRepository = blockRepository;
        this.txController = txController;
        ifBlockchainNotExist();
    }

    public void ifBlockchainNotExist() {
        Block lastBlock = blockRepository.getLastBlock();
        if (lastBlock == null) this.createNewBlockchain("duke");
    }

    public void createNewBlockchain(String address){
        Transaction coinbase = txController.newCoinbaseTX(address);
        newGenesisBlock(coinbase);
    }

    public void newGenesisBlock(Transaction coinbase) {
        Block genesisBlock = new Block(List.of(coinbase), new byte[]{});
        addBlock(genesisBlock);
    }

    public void addBlock(List<Transaction> data) {
        Block lastBlock = this.blockRepository.getLastBlock();
        Block newBlock = new Block(data, lastBlock.getHash());
        addBlock(newBlock);
    }

    public void addBlock(Block newBlock) {
        if (newBlock.getHash() == null) miningBlock(newBlock);
        this.blockRepository.addNewBlock(newBlock);
    }

    public void printBlockchain() {
        Block block = this.blockRepository.getLastBlock();
        logger.info(block.toString());

        while (block.getPrevHash().length != 0) {
            block = this.blockRepository.getPreviousBlock(block);
            logger.info(block.toString());
        }
    }

    private static String prepareData(Block block, int nonce) {
        return Hex.toHexString(block.getPrevHash()) +
                block.getTimeStamp().toString() +
                block.getHashTransactions() +
                nonce;
    }

    public static void miningBlock(Block block) {
        logger.info("Mining Block of Transactions: {}", block.getHashTransactions());

        byte[] difficult = new byte[64];
        Arrays.fill(difficult, (byte) 0x00);
        difficult[3] = (byte) 0x80;

        int nonce = -1;
        byte[] hash;

        do {
            nonce++;
            String header = prepareData(block, nonce);
            hash = HashUtil.sha256(header);
        } while (Arrays.compare(hash, difficult) > 0);

        logger.info("Hash is {} with nonce {}", hash, nonce);
        block.setNonce(nonce);
        block.setHash(hash);
    }

}
