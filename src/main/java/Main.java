
import controllers.BlockController;
import controllers.TransactionController;
import core.Transaction;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BlockRepository;

import java.io.File;

public class Main {

    private static final String DBFILE = "Database/blockchain.db";

    public static void main(String[] args) {
        DB db = DBMaker.fileDB(new File(DBFILE))
                .closeOnJvmShutdown()
                .transactionEnable()
                .make();

        BlockRepository blockRepository = new BlockRepository(db);
        TransactionController transactionController = new TransactionController(blockRepository);
        BlockController blockController = new BlockController(blockRepository, transactionController);

    }
}
