package xxx.joker.apps.tagmod.util;


import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.libs.core.utils.JkBytes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.barbano on 11/02/2018.
 */
public class BytesScanner {

	private byte[] bytes;
	private int seekOffset;

	private BytesScanner(byte[] bytes) {
		this.bytes = bytes;
		this.seekOffset = 0;
	}

	public static BytesScanner getScanner(byte[] bytes) {
		return new BytesScanner(bytes);
	}
//	public static BytesScanner getScanner(RandomAccessFile raf, int length) throws IOException {
//		return getScanner(raf, 0, length);
//	}
	public static BytesScanner getScanner(RandomAccessFile raf, int start, int length) throws IOException {
		byte[] bytes = JkBytes.getBytes(raf, start, length);
		return new BytesScanner(bytes);
	}

	public void seek(int position) {
		this.seekOffset = position;
	}
	public void skip(int offset) {
		this.seekOffset += offset;
	}
	public void skip(TxtEncoding encoding) {
		this.seekOffset += encoding.getStringStop().length;
	}

	public int length() {
		return bytes.length;
	}
	public int remaining() {
		return bytes.length - seekOffset;
	}
	public int position() {
		return seekOffset;
	}


	public byte getByte(int offset) {
		return bytes[seekOffset + offset];
	}
	public int getByteInt(int offset) {
		return TmFormat.toInt(getByte(offset));
	}
	public byte[] getBytes(int start, int length) {
		return getBytes(start, length, false);
	}
	public byte[] getBytes(int start, int length, boolean unsynch) {
		byte[] bytes = getAllBytes(start, length, unsynch);
		return TmFormat.removeUnsynchronisation(bytes);
	}
	private byte[] getAllBytes(int start, int length, boolean unsynch) {
		int idxStart = start + seekOffset;
		byte[] bytes = unsynch ? getBytesUnsynch(idxStart, length) : JkBytes.getBytes(this.bytes, idxStart, length);
		return bytes;
	}
	private byte[] getBytesUnsynch(int start, int length) {
		List<Byte> list = new ArrayList<>();

		int counter = start;
		byte actual = 0;
		for(int rem = length; rem > 0; counter++) {
			actual = bytes[counter];
			list.add(actual);
			if(counter == start) {
				rem--;
			} else {
				byte last = bytes[counter-1];
				if(actual != (byte)0x00 || last != (byte)0xFF) {
					rem--;
				}
			}
		}

		if(JkBytes.isEquals(actual, 0xFF) && JkBytes.isEquals(bytes[counter], 0x00)) {
			list.add((byte)0x00);
		}

		return JkBytes.toByteArray(list);
	}

	public byte nextByte() {
		byte b = getByte(0);
		skip(1);
		return b;
	}
	public int nextByteInt() {
		int b = getByteInt(0);
		skip(1);
		return b;
	}
	public byte[] nextBytes() {
		return nextBytes(remaining(), false);
	}
	public byte[] nextBytes(int length) {
		return nextBytes(length, false);
	}
	public byte[] nextBytes(int length, boolean unsynch) {
		byte[] bytes = getAllBytes(0, length, unsynch);
		skip(bytes.length);
		return unsynch ? TmFormat.removeUnsynchronisation(bytes) : bytes;
	}
	public BytesScanner nextSubScanner(int length, boolean unsynch) {
		byte[] bytes = nextBytes(length, unsynch);
		return getScanner(bytes);
	}

	public String getString(int begin, int length) {
		return getString(begin, length, null);
	}
	public String getString(int begin, int length, TxtEncoding encoding) {
		return TmFormat.toString(bytes, begin + seekOffset, length, encoding);
	}

	public String nextString(int length) {
		return nextString(length, null);
	}
	public String nextString(int length, TxtEncoding encoding) {
		String str = getString(0, length, encoding);
		skip(length);
		return str;
	}

	public String nextSeparatedString(TxtEncoding encoding) {
		return nextSeparatedString(encoding, remaining());
	}
	public String nextSeparatedString(TxtEncoding encoding, int maxLength) {
		int idx = findFirstByteArray(encoding.getStringStop(), 0, maxLength, false);
		return nextString(idx == -1 ? maxLength : idx, encoding);
	}
	public String nextSeparatedString(int separator) {
		return nextSeparatedString(separator, remaining());
	}
	public String nextSeparatedString(int separator, int maxLength) {
		int idx = findFirstByte((byte)separator, 0, maxLength, false);
		return nextString(idx == -1 ? maxLength : idx);
	}

	public String asString(TxtEncoding encoding) {
		return nextString(remaining(), encoding);
	}

	public int findLastDifferentByte(int toFind) {
		for(int i = bytes.length - 1; i >= seekOffset; i--) {
			if(toFind != bytes[i]) {
				return i - seekOffset;
			}
		}
		return -1;
	}

	public int findString(String str) {
		byte[] toFind = TmFormat.toBytes(str);
		return findFirstByteArray(toFind, 0, -1, false);
	}

	private int findFirstByte(byte toFind, int offset, int maxLength, boolean isUnsynch) {
		return findFirstByteArray(new byte[] {toFind}, offset, maxLength, isUnsynch);
	}
	private int findFirstByteArray(byte[] toFind, int offset, int maxLength, boolean isUnsynch) {
		int startOffset = seekOffset + offset;
		int rem = maxLength == -1 ? length() - startOffset : maxLength;

		for(int i = startOffset; i < length() && rem > 0;) {
			int pos = i;
			int synchBytes = 0;
			// Find 1st byte of 'toFind'
			while (pos < length() && rem > 0)	 {
				if(isUnsynch && TmFormat.isSynchByte(bytes, pos)) {
					synchBytes++;
				} else {
					if(JkBytes.isEquals(bytes[pos], toFind[0])) {
						break;
					}
					rem--;
				}
				pos++;
			}

			if(pos == length() || rem == 0) {
				return -1;
			}

			if(toFind.length == 1) {
				return pos - startOffset - synchBytes;
			}

			int start = pos;

			// Find for 'toFind'
			int subRem = rem;
			int eq = 0;
			for(; eq < toFind.length && pos+toFind.length <= length() && subRem > 0; pos++) {
				if(!isUnsynch || !TmFormat.isSynchByte(bytes, pos)) {
					if(!JkBytes.isEquals(bytes[pos], toFind[eq])) {
						break;
					}
					eq++;
					subRem--;
				} else {
					synchBytes++;
				}
			}

			if(eq == toFind.length) {
				return start - startOffset - synchBytes;
			}

			i = start + 1;
			rem--;
		}

		return -1;
	}
}
