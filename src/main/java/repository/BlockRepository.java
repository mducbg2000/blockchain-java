package repository;

import core.Block;
import org.apache.commons.lang3.SerializationUtils;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class BlockRepository {
    private final HTreeMap<byte[], byte[]> blocks;
    private final DB db;
    private final static Logger logger = LoggerFactory.getLogger(BlockRepository.class);
    private final static byte[] LAST = "last".getBytes(StandardCharsets.UTF_8);

    public BlockRepository(DB db) {
        this.db = db;
        this.blocks = this.db.hashMap("blocks")
                .keySerializer(Serializer.BYTE_ARRAY)
                .valueSerializer(Serializer.BYTE_ARRAY)
                .createOrOpen();
    }

    public Block getLastBlock() {
        if (this.blocks.size() == 0) {
            return null;
        }
        byte[] lastHash = this.blocks.get(LAST);
        return getBlock(lastHash);
    }

    public void addNewBlock(Block newBlock) {
        byte[] bytes = SerializationUtils.serialize(newBlock);
        this.blocks.put(newBlock.getHash(), bytes);
        this.blocks.put(LAST, newBlock.getHash());
        this.db.commit();
    }

    public Block getPreviousBlock(Block block) {
        byte[] previousHash = block.getPrevHash();
        if (previousHash == null || previousHash.length == 0) return null;
        return getBlock(previousHash);
    }

    public Block getBlock(byte[] hash) {
        if (hash == null) return null;
        Block block;
        try {
            block = SerializationUtils.deserialize(Objects.requireNonNull(this.blocks.get(hash)));
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
        return block;
    }


}
