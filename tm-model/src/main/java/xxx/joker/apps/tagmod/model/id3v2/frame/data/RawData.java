package xxx.joker.apps.tagmod.model.id3v2.frame.data;

import java.util.Arrays;

import static xxx.joker.libs.core.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class RawData implements IFrameData {

	private byte[] data;

	public RawData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public boolean isDataDuplicated(IFrameData frameData) {
		return false;
	}

	@Override
	public String toStringInline() {
		return strf("data size: %d", data.length);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RawData)) return false;

		RawData rawData = (RawData) o;

		return Arrays.equals(data, rawData.data);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}
}
