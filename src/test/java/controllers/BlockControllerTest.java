package controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BlockControllerTest {

	@Test
	public void testCompareByteArr() {
		byte[] first = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x11, (byte) 0xfc};
		byte[] second = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0xfa};
		System.out.println(Arrays.compare(first, second));
	}

}
