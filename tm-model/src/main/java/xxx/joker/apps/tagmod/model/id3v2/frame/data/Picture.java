package xxx.joker.apps.tagmod.model.id3v2.frame.data;


import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.PictureType;

import java.util.Arrays;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class Picture implements IFrameData {

	private MimeType mimeType;
	private PictureType picType;
	private String description = "";
	private byte[] picData;

	public Picture() {
	}

	public Picture(MimeType mimeType, PictureType picType, String description, byte[] picData) {
		this.mimeType = mimeType;
		this.picType = picType;
		this.description = description;
		this.picData = picData;
	}

	public MimeType getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}

	public PictureType getPicType() {
		return picType;
	}

	public void setPicType(PictureType picType) {
		this.picType = picType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getPicData() {
		return picData;
	}

	public void setPicData(byte[] picData) {
		this.picData = picData;
	}

	@Override
	public String toString() {
		return toStringInline();
	}

	@Override
	public boolean isDataDuplicated(IFrameData frameData) {
		if(!(frameData instanceof Picture)) {
			return false;
		}

		Picture other = (Picture) frameData;
		if(picType == other.picType && Arrays.asList(PictureType.FILE_ICON, PictureType.OTHER_FILE_ICON).contains(picType)) {
			return true;
		}

		return description.equals(other.description);
	}

	@Override
	public String toStringInline() {
		return strf("%s, %s (%d), *%s*, size=%d",
			mimeType.name(),
			picType == null ? "null" : picType.normalizedName(),
			picType == null ? -1 : picType.pictureNumber(),
			description,
			picData.length
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Picture)) return false;

		Picture picture = (Picture) o;

		if (mimeType != picture.mimeType) return false;
		if (picType != picture.picType) return false;
		if (!StringUtils.equalsIgnoreCase(description, picture.description)) return false;
		return Arrays.equals(picData, picture.picData);
	}

	@Override
	public int hashCode() {
		int result = mimeType != null ? mimeType.hashCode() : 0;
		result = 31 * result + (picType != null ? picType.hashCode() : 0);
		result = 31 * result + (description != null ? description.toLowerCase().hashCode() : 0);
		result = 31 * result + Arrays.hashCode(picData);
		return result;
	}

}
