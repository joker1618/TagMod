package xxx.joker.apps.tagmod.model.id3v2.frame.data;

import org.apache.commons.lang3.StringUtils;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class TextInfo implements IFrameData {

	private String info = "";

	public TextInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return info;
	}

	@Override
	public boolean isDataDuplicated(IFrameData frameData) {
		return (frameData instanceof TextInfo);
	}

	@Override
	public String toStringInline() {
		return strf("*%s*", info);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TextInfo)) return false;

		TextInfo textInfo = (TextInfo) o;
		return StringUtils.equalsIgnoreCase(info, textInfo.info);
	}

	@Override
	public int hashCode() {
		return info != null ? info.toLowerCase().hashCode() : 0;
	}
}
