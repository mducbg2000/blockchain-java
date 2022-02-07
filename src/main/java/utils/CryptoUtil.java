package utils;

import controllers.TransactionController;
import core.Transaction;
import org.apache.commons.lang3.ArrayUtils;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptoUtil {
	private static final String SPEC = "secp256k1";
	private static final String ALGORITHM = "EC";
	private static final String SIGNATURE = "SHA256withECDSA";

	public static KeyPair newKeyPair() {
		ECGenParameterSpec spec = new ECGenParameterSpec(SPEC);
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance(ALGORITHM);
			generator.initialize(spec);
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e){
			throw new IllegalStateException(e.getMessage());
		}
		return generator.genKeyPair();
	}

	public static byte[] getPubKeyHashFromAddress(String address) {
		byte[] payload = Base58.decode(address);
		return ArrayUtils.subarray(payload, 1, payload.length - 4);
	}

	public static Signature getSignature() {
		try {
			return Signature.getInstance(SIGNATURE);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

}
