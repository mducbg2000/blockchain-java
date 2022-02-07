package controllers;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import repository.BlockRepository;

public class CLIControllerTest {

    private CLIController cliController;

    @BeforeEach
    public void init() {
        DB db = DBMaker.fileDB("Database/Test5.db").closeOnJvmShutdown().make();
        BlockRepository blockRepository = new BlockRepository(db);
        TransactionController transactionController = new TransactionController(blockRepository);
        BlockController blockController = new BlockController(blockRepository, transactionController);
        cliController = new CLIController(blockController, transactionController);
    }

    @Test
    public void testPrintHelp() {
        cliController.printHelp();
    }

    @Test
    public void testGetBalance() throws ParseException {
        String[] args = new String[]{"-b", "bob"};
        cliController.CLIParser(args);
    }

    @Test
    public void testSendCoin() throws ParseException {
        String[] args = new String[]{"--send", "-Dfrom=jax", "-Dto=bob", "-Damount=2"};
        cliController.CLIParser(args);
    }

    @Test
    public void testPrintBlockchain() throws ParseException {
        String[] args = new String[]{"-p"};
        cliController.CLIParser(args);
    }

}
