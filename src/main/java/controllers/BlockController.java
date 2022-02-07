package controllers;

import core.Block;
import core.ProofOfWork;
import core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepository;
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
        Transaction coinbase = txController.newCoinbaseTX(address, "Genesis coinbase!");
        newGenesisBlock(coinbase);
    }

    public void newGenesisBlock(Transaction coinbase) {
        Block genesisBlock = new Block(List.of(coinbase), "");
        addBlock(genesisBlock);
    }

    public void addBlock(List<Transaction> data) {
        Block lastBlock = this.blockRepository.getLastBlock();
        Block newBlock = new Block(data, lastBlock.getHash());
        addBlock(newBlock);
    }

    public void addBlock(Block newBlock) {
        if(newBlock.getHash() == null) ProofOfWork.miningBlock(newBlock);
        this.blockRepository.addNewBlock(newBlock);
    }

    public void printBlockchain() {
        Block block = this.blockRepository.getLastBlock();
        logger.info(block.toString());

        while (block.getPrevHash().length() != 0) {
            block = this.blockRepository.getPreviousBlock(block);
            logger.info(block.toString());
        }
    }


}
