package xxx.joker.apps.tagmod.util;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.libs.core.utils.JkBytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by f.barbano on 17/01/2018.
 */
public class ByteBuilder {

	private List<byte[]> byteList;

	public ByteBuilder() {
		this.byteList = new ArrayList<>();
	}

	public ByteBuilder(byte[] bytes) {
		this.byteList = new ArrayList<>();
		this.byteList.addAll(Arrays.asList(bytes));
	}

	public ByteBuilder add(byte b) {
		return add(new byte[]{b});
	}

	public ByteBuilder insertFirst(byte[] arr) {
		if(arr != null) {
			byteList.add(0, arr);
		}
		return this;
	}
	public ByteBuilder add(byte[] arr) {
		if(arr != null)	{
			byteList.add(arr);
		}
		return this;
	}
	public ByteBuilder addStringStop(TxtEncoding encoding) {
		byteList.add(encoding.getStringStop());
		return this;
	}
	public ByteBuilder addZeroBytes(int num) {
		byteList.add(new byte[num]);
		return this;
	}

	public ByteBuilder add(String str) {
		return add(TmFormat.toBytes(str));
	}
	public ByteBuilder add(String str, TxtEncoding encoding) {
		return add(TmFormat.toBytes(str, encoding));
	}
	public ByteBuilder add(String str, int fixedLength) {
		byte[] res;
		if(StringUtils.isEmpty(str)) {
			res = new byte[fixedLength];
		} else {
			byte[] bytes = TmFormat.toBytes(str);
			res = JkBytes.getBytes(bytes, 0, fixedLength);
		}
		return add(res);
	}
	public ByteBuilder addLastFix(String str, int fixedLength) {
		byte[] res;
		if(StringUtils.isEmpty(str)) {
			res = new byte[fixedLength];
		} else {
			byte[] bytes = TmFormat.toBytes(str);
			res = JkBytes.getBytes(bytes, 0, fixedLength);
			for(int i = res.length-1; i >= 0; i--) {
			    if(res[i] == ' ') {
                    res[i] = 0;
                } else {
			        break;
                }
            }
		}
		return add(res);
	}

	public ByteBuilder add(int num) {
		return add((byte)num);
	}

	public byte[] build() {
		return JkBytes.mergeArrays(byteList);
	}

	public int length() {
		return byteList.stream().mapToInt(arr -> arr.length).sum();
	}
}
