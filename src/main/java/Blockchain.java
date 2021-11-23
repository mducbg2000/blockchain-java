import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> blocks;

    public Blockchain() {
        this.blocks = new ArrayList<>();
        Block genesisBlock = genesisBlock();
        this.blocks.add(genesisBlock);
    }

    public void addBlock(String data) {
        Block lastBlock = this.blocks.get(blocks.size() - 1);
        Block newBlock = new Block(data, lastBlock.getHash());
        this.blocks.add(newBlock);
    }

    private Block genesisBlock() {
        return new Block("Genesis Block", "0");
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
