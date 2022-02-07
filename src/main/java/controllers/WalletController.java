package controllers;

import core.Wallet;
import org.apache.commons.lang3.ArrayUtils;
import utils.Base58;
import utils.HashUtil;
import utils.CryptoUtil;

import java.security.KeyPair;

public class WalletController {

	private static final byte[] version = {(byte)0x00};

	public Wallet newWallet() {
		KeyPair keyPair = CryptoUtil.newKeyPair();
		return new Wallet(keyPair);
	}

	public String getAddress(Wallet wallet) {
		byte[] pubKeyHash = HashUtil.hashPubKey(wallet.getPublicKey());
		byte[] versionPayload = ArrayUtils.addAll(version, pubKeyHash);
		byte[] fullPayload = ArrayUtils.addAll(versionPayload, checksum(versionPayload));
		return Base58.encode(fullPayload);
	}

	public byte[] checksum(byte[] payload) {
		byte[] firstSHA = HashUtil.sha256(payload);
		byte[] secondSHA = HashUtil.sha256(firstSHA);
		return ArrayUtils.subarray(secondSHA, 0, 4);
	}


}
