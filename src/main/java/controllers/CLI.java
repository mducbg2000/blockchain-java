package controllers;

import org.apache.commons.cli.*;
import org.apache.commons.codec.DecoderException;

public class CLI {

    private final BlockController controller;

    public CLI() {
        this.controller = new BlockController();
    }

    public void cliParser(String[] args) throws ParseException, DecoderException {
        Options options = new Options();
        Option addBlock = Option.builder("a")
                .longOpt("addBlock")
                .argName("data")
                .hasArg()
                .desc("Add new block")
                .build();
        options.addOption(addBlock);
        options.addOption("p", false, "Print blockchain");
        options.addOption("h", false, "Print help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter help = new HelpFormatter();

        if (cmd.hasOption("a")) {
            String data = cmd.getOptionValue("a");
            this.controller.addBlock(data);
        }

        if (cmd.hasOption("p"))
            this.controller.printBlockchain();

        if (cmd.hasOption("h")) {
            help.printHelp("-h", options, false);
        }
    }

}
