package yoshikihigo.jmc.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Hash {

	public static Hash getMD5(final String text) {
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] data = text.getBytes(StandardCharsets.UTF_8);
			md.update(data);
			final byte[] digest = md.digest();
			return new Hash(digest);
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new Hash(new byte[0]);
		}
	}

	final public byte[] value;

	public Hash(final byte[] value) {
		this.value = Arrays.copyOf(value, value.length);
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Hash)) {
			return false;
		}
		final Hash target = (Hash) o;
		return Arrays.equals(this.value, target.value);
	}

}
