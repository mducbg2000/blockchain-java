package repository;

import core.Block;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;
import org.mapdb.DB;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentMap;

public class BlockRepository {
    private final ConcurrentMap<String, String> blocks;
    private final DB db;
    private final static Logger logger = LoggerFactory.getLogger(BlockRepository.class);

    public BlockRepository(DB db) {
        this.db = db;
        this.blocks = this.db.hashMap("blocks")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .createOrOpen();
    }

    public Block getLastBlock() {
        if (this.blocks.size() == 0) {
            return null;
        }
        String lastHash = this.blocks.get("last");
        return getBlock(lastHash);
    }

    public void addNewBlock(Block newBlock) {
        byte[] bytes = SerializationUtils.serialize(newBlock);
        String hexBlock = Hex.encodeHexString(bytes);
        this.blocks.put(newBlock.getHash(), hexBlock);
        this.blocks.put("last", newBlock.getHash());
        this.db.commit();
    }

    public Block getPreviousBlock(Block block) {
        String previousHash = block.getPrevHash();
        if (previousHash == null || previousHash.equals("")) return null;
        return getBlock(previousHash);
    }

    public Block getBlock(String hash) {
        if (hash == null) return null;
        Block block = null;
        try {
            block = SerializationUtils.deserialize(Hex.decodeHex(this.blocks.get(hash)));
        } catch (DecoderException e) {
            logger.info(e.getMessage());
        }
        return block;
    }


}
