package utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base58Test {

	@Test
	 public void testEncode() {
		String result = Base58.encode("test".getBytes(StandardCharsets.UTF_8));
		assertEquals(result, "3yZe7d");
	}

	@Test
	public void testDecode() {
		String result = new String(Base58.decode("3yZe7d"));
		assertEquals(result, "test");
	}


}
