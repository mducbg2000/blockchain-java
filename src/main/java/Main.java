import controllers.CLI;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ParseException, DecoderException {
        CLI cli = new CLI();
        cli.cliParser(args);
    }
}
