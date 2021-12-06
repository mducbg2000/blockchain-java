import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void testAddCLI() throws DecoderException, ParseException {
        String[] args = new String[]{"-a", "Test open file database"};
        Main.main(args);
    }

    @Test
    public void testPrintCLI() throws DecoderException, ParseException {
        String[] args = new String[]{"-p"};
        Main.main(args);
    }

    @Test
    public void testPrintHelp() throws DecoderException, ParseException {
        String[] args = new String[]{"-h"};
        Main.main(args);
    }
}
