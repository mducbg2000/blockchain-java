package controllers;
import core.Transaction;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Properties;

public class CLIController {

    private final BlockController blockController;
    private final TransactionController transactionController;
    private Options options;

    public CLIController(BlockController blockController, TransactionController transactionController) {
        this.blockController = blockController;
        this.transactionController = transactionController;
        this.buildOption();
    }


    public void CLIParser(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args);

        if (cli.hasOption('p')) {
            blockController.printBlockchain();
        }
        if (cli.hasOption('s')) {
            Properties properties = cli.getOptionProperties("D");
            String from = properties.getProperty("from");
            String to = properties.getProperty("to");
            int amount = Integer.parseInt(properties.getProperty("amount"));
            Transaction transaction = transactionController.newTransaction(from, to, amount);
            if (transaction == null) return;
            blockController.addBlock(List.of(transaction));
            System.out.println("Success!");
        }
        if (cli.hasOption('b')) {
            String address = cli.getOptionValue('b');
            transactionController.getBalance(address);
        }

    }

    public void buildOption() {
        Options options = new Options();

        Option print = new Option("p", "Print Blockchain");
        options.addOption(print);

        Option send = new Option("s", "send", false, "Send coin");
        options.addOption(send);

        Option sendProperties = Option.builder("D")
                .hasArgs()
                .numberOfArgs(2)
                .argName("address=value")
                .valueSeparator()
                .desc("Properties of a transaction, use with send option " +
                        "\n Ex: -s -Dfrom=Bob -Dto=Alice -Damount=5")
                .build();
        options.addOption(sendProperties);

        Option balance = Option.builder("b")
                .longOpt("balance")
                .hasArg()
                .argName("address")
                .numberOfArgs(1)
                .desc("Get balance of an address")
                .build();
        options.addOption(balance);
        this.options = options;
    }

    public void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Blockchain-java", options);
    }

}
