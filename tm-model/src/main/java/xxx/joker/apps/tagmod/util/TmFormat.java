package xxx.joker.apps.tagmod.util;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.exceptions.InvalidArgError;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.libs.core.utils.JkBytes;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.barbano on 11/02/2018.
 */
public class TmFormat {

	// Path format
	public static boolean isMP3File(Path filePath) {
		return Files.isRegularFile(filePath) && StringUtils.endsWithIgnoreCase(filePath.toString(), ".mp3");
	}
	public static boolean isMP3Lyrics(Path filePath) {
		return Files.isRegularFile(filePath) && StringUtils.endsWithIgnoreCase(filePath.toString(), ".lyrics");
	}

	// Output format
	public static int toInt(byte b) {
		return b & 0xFF;
	}
	public static short toShort(byte b) {
		return (short) toInt(b);
	}


	public static String toString(byte[] bytes) {
		return toString(bytes, null);
	}
	public static String toString(byte[] bytes, TxtEncoding encoding) {
		return toString(bytes, 0, bytes.length, encoding);
	}
	public static String toString(byte[] bytes, int start, int length, TxtEncoding encoding) {
		Charset enc = getCharset(encoding);
		return new String(bytes, start, length, enc).trim();
	}

	public static byte[] toBytes(String str) {
		return toBytes(str, null);
	}
	public static byte[] toBytes(String str, TxtEncoding encoding) {
		Charset enc = getCharset(encoding);
		return str.getBytes(enc);
	}

	private static Charset getCharset(TxtEncoding encoding) {
		return encoding == null ? ID3Specs.DEFAULT_ENCODING.getCharset() : encoding.getCharset();
	}


	// Number parsing
	public static int compute24BitSize(byte[] bytes) {
		return computeNumber(bytes, 3);
	}
	public static int compute32BitSize(byte[] bytes) {
		return computeNumber(bytes, 4);
	}
	public static int compute32BitSynchsafeSize(byte[] bytes) {
		if(bytes.length != 4) {
			throw new InvalidArgError("Input byte array must have length == 4. Found %d", bytes.length);
		}

		int size = (TmFormat.toShort(bytes[0])) << 21;
		size += (TmFormat.toShort(bytes[1])) << 14;
		size += (TmFormat.toShort(bytes[2])) << 7;
		size += (TmFormat.toShort(bytes[3]));

		return size;
	}
	private static int computeNumber(byte[] bytes, int start, int end) {
		int size = 0;
		for(int i = start, exp = end - start - 1; exp >= 0; i++, exp--) {
			size += (TmFormat.toShort(bytes[i])) << (exp * 8);
		}
		return size;
	}
	private static int computeNumber(byte[] bytes, int expectedBytes) {
		if(bytes.length != expectedBytes) {
			throw new InvalidArgError("Expected array size %d, found %d!", expectedBytes, bytes.length);
		}
		return computeNumber(bytes, 0, bytes.length);
	}


	// Number to bytes
	public static byte[] numberTo24Bit(int num) {
		return numberToBytes(num, 3);
	}
	public static byte[] numberTo32Bit(int num) {
		return numberToBytes(num, 4);
	}
	public static byte[] numberTo32BitSynchsafe(int num) {
		byte[] barr = new byte[] {(byte) toSynchsafeInt((byte) (num >>> 21)),
			(byte) toSynchsafeInt((byte) (num >>> 14)),
			(byte) toSynchsafeInt((byte) (num >>> 7)),
			(byte) toSynchsafeInt((byte) num)};

		return barr;
	}
	private static byte[] numberToBytes(int num, int arrayLength) {
		byte[] barr = new byte[arrayLength];
		for(int i=0, exp=arrayLength-1; i<arrayLength; i++, exp--) {
			barr[i] = (byte) (num >>> (exp*8));
		}
		return barr;
	}
	private static int toSynchsafeInt(byte b) {
		return (short) (JkBytes.isBitSet(b, 7) ? (b ^ 0x80) : b);
	}


	// Unsynchronisation management
	public static byte[] removeUnsynchronisation(byte[] source) {
		byte[] toRet = new byte[source.length];
		int idx = 0;
		for(int i = 0; i < source.length; i++) {
			if(!isSynchByte(source, i)) {
				toRet[idx] = source[i];
				idx++;
			}
		}
		return JkBytes.getBytes(toRet, 0, idx);
	}
	public static byte[] addUnsynchronisation(byte[] source) {
		List<Byte> list = new ArrayList<>();
		for(int i = 0; i < source.length; i++) {
			list.add(source[i]);
			if(JkBytes.isEquals(source[i], 0xFF)) {
				list.add((byte)0x00);
			}
		}
		return JkBytes.toByteArray(list);
	}
	public static boolean isSynchByte(byte[] bytes, int offset) {
		return offset > 0 && offset < bytes.length
				   && JkBytes.isEquals(bytes[offset-1], 0xFF)
				   && JkBytes.isEquals(bytes[offset], 0x00);
	}

}
