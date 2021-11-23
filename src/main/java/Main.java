import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock("Transaction 1: A send B 1 coin");
        blockchain.addBlock("Transaction 2: B send A 3 coin");
        blockchain.getBlocks().forEach(block -> {
            logger.info(block.toString());
        });
    }
}
