package controller;

import controllers.BlockController;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BlockControllerTest {

    private static BlockController blockController;

    @BeforeAll
    static void setup(){
        blockController = new BlockController();
    }

    @Test
    void testAddNewBlock() throws DecoderException {
        blockController.addBlock("Test case add block");
    }

}
