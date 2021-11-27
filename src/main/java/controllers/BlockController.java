package controllers;

import core.Block;
import core.ProofOfWork;
import org.apache.commons.codec.DecoderException;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepo;
import repository.Database;

public class BlockController {
    private final BlockRepo blockRepo;
    private final static Logger logger = LoggerFactory.getLogger(BlockController.class);

    public BlockController() {
        DB db = Database.getDB();
        this.blockRepo = new BlockRepo(db);
    }

    public Block addGenesisBlock() {
        Block genesisBlock = new Block("Genesis Block", "0");
        addBlock(genesisBlock);
        return genesisBlock;
    }

    public void addBlock(String data) throws DecoderException {
        Block lastBlock = this.ifEmpty();
        Block newBlock = new Block(data, lastBlock.getHash());
        addBlock(newBlock);
    }

    public void addBlock(Block newBlock) {
        if(newBlock.getHash() == null)
            ProofOfWork.miningBlock(newBlock);
        this.blockRepo.addNewBlock(newBlock);
    }

    public void printBlockchain() throws DecoderException {
        Block block = this.ifEmpty();
        logger.info(block.toString());

        while (!block.getPrevHash().equals("0")) {
            block = this.blockRepo.getPreviousBlock(block);
            logger.info(block.toString());
        }
    }

    public Block ifEmpty() throws DecoderException {
        Block lastBlock = this.blockRepo.getLastBlock();
        if (lastBlock == null) {
            logger.info("Not have any block yet, creating new blockchain with genesis block");
            lastBlock = this.addGenesisBlock();
        }
        return lastBlock;
    }

}
