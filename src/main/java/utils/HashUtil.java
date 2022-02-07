package utils;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

public class HashUtil {


	public static byte[] hashPubKey(PublicKey publicKey) {
		byte[] sha256Key = sha256(publicKey.getEncoded());
		return ripeMD160(sha256Key);
	}

	public static String sha256ToHex(String data) {
		return Hex.toHexString(sha256(data));
	}

	public static byte[] sha256(String data) {
		return sha256(data.getBytes(StandardCharsets.UTF_8));
	}

	public static byte[] sha256(byte[] data) {
		SHA256Digest digest = new SHA256Digest();
		return hash(data, digest);
	}

	public static byte[] ripeMD160(byte[] data) {
		RIPEMD160Digest digest = new RIPEMD160Digest();
		return hash(data, digest);
	}

	private static byte[] hash(byte[] data, GeneralDigest digest) {
		digest.update(data, 0, data.length);
		byte[] out = new byte[digest.getDigestSize()];
		digest.doFinal(out, 0);
		return out;
	}

}
