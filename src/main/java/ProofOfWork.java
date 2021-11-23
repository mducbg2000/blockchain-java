import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProofOfWork {
    private static final int DIFFICULTY = 4;

    private static final Logger logger = LoggerFactory.getLogger(ProofOfWork.class);

    private static String prepareData(Block block, int nonce) {
        return block.getPrevHash() +
                block.getTimeStamp().toString() +
                block.getData() +
                nonce;
    }

    public static void miningBlock(Block block) {
        logger.info("Mining Block of Data: {}", block.getData());
        String prefix = new String(new char[DIFFICULTY]).replace('\0', '0');
        int nonce = -1;
        String hash;
        do {
            nonce++;
            String header = prepareData(block, nonce);
            hash = Utils.hash(header);
        } while (!hash.substring(0, DIFFICULTY).equals(prefix));
        logger.info("Hash is {} with nonce {}", hash, nonce);
        block.setNonce(nonce);
        block.setHash(hash);
    }

}
