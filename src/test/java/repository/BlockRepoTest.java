package repository;

import core.Block;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BlockRepoTest {

    private static BlockRepo blockRepo;
    static final Logger logger = LoggerFactory.getLogger(BlockRepoTest.class);

    @BeforeAll
    static void setup() {
        DB db = Database.getDB();
        blockRepo = new BlockRepo(db);
    }

    @Test
    void testGetLastBlock() throws DecoderException {
        Block block = blockRepo.getLastBlock();
        logger.info(block.toString());
    }
}
