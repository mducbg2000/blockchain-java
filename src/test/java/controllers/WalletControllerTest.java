package controllers;

import core.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletControllerTest {

	private WalletController walletController;

	@BeforeEach
	void setUp() {
		walletController = new WalletController();
	}

	@Test
	public void testGetAddress() {
		Wallet wallet = walletController.newWallet();
		String result = walletController.getAddress(wallet);
		System.out.println(result);
		assertEquals(result.length(), "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa".length());
	}
}
