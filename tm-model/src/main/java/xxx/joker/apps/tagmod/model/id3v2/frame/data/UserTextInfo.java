package xxx.joker.apps.tagmod.model.id3v2.frame.data;

import org.apache.commons.lang3.StringUtils;

import static xxx.joker.libs.core.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class UserTextInfo implements IFrameData {

	private String description = "";
	private String info = "";

	public UserTextInfo() {
	}

	public UserTextInfo(String description, String info) {
		this.description = description;
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return toStringInline();
	}

	@Override
	public boolean isDataDuplicated(IFrameData frameData) {
		if(!(frameData instanceof UserTextInfo)) {
			return false;
		}

		UserTextInfo other = (UserTextInfo) frameData;
		return description.equals(other.description);
	}

	@Override
	public String toStringInline() {
		return strf("*%s*, *%s*", description, info);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserTextInfo)) return false;

		UserTextInfo that = (UserTextInfo) o;

		return StringUtils.equalsIgnoreCase(description, that.description)
			&& StringUtils.equalsIgnoreCase(info, that.info);
	}

	@Override
	public int hashCode() {
		int result = description != null ? description.toLowerCase().hashCode() : 0;
		result = 31 * result + (info != null ? info.toLowerCase().hashCode() : 0);
		return result;
	}

}
