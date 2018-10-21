package xxx.joker.apps.tagmod.model.beans;

import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.UserTextInfo;
import xxx.joker.apps.tagmod.model.mp3.MP3File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

/**
 * ID3v2:
 *   An ID3 frame TXXX, the last, with
 *   - description = {@link #ID3v2_SIGN_DESCRIPTION}
 *   - value = TAG_TIME(yyyyMMddHHmm)
 *
 * ID3v1:	comments field will be
 */
public class TmSignature {

	private static final String ID3v1_SIGN_PREFIX = "TagMod (J@K3R)";

	private static final String ID3v2_SIGN_DESCRIPTION = "TagMod (J@K3R) tag time";

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private LocalDateTime tagTime;

	public TmSignature(LocalDateTime tagTime) {
		this.tagTime = tagTime;
	}

//	public static LocalDateTime parseSignature(MP3File mp3file) {
//		LocalDateTime tagTimeV2 = getTagTimeV2(mp3file);
//		LocalDateTime tagTimeV1 = getTagTimeV1(mp3file);
//		return tagTimeV2 == null || tagTimeV1 == null ? null : tagTimeV2;
//	}
//	private static LocalDateTime getTagTimeV2(MP3File mp3file) {
//		List<TAGv2> t2list = mp3file.getTAGv2List();
//		if(t2list.size() != 1)		return null;
//
//		List<ID3v2Frame> frameList = t2list.get(0).getFrameList();
//		if(frameList.isEmpty())		return null;
//
//		ID3v2Frame frame = frameList.get(frameList.size() - 1);
//		if(!(frame.getFrameData() instanceof UserTextInfo))	{
//			return null;
//		}
//
//		UserTextInfo userTextInfo = frame.getFrameData();
//		if(!ID3v2_SIGN_DESCRIPTION.equals(userTextInfo.getDescription())) {
//			return null;
//		}
//
//		try {
//			return LocalDateTime.parse(userTextInfo.getInfo(), dtf);
//		} catch (Exception ex) {
//			return null;
//		}
//	}
//	private static LocalDateTime getTagTimeV1(MP3File mp3file) {
//		TAGv1 tag1 = mp3file.getTAGv1();
//		if(tag1 == null)	return null;
//
//		if(!tag1.getComments().startsWith(ID3v1_SIGN_PREFIX))	return null;
//
//		try {
//			String strTagTime = tag1.getComments().replace(ID3v1_SIGN_PREFIX, "").trim();
//			return LocalDateTime.parse(strTagTime, dtf);
//		} catch (Exception ex) {
//			return null;
//		}
//	}
//
//	public UserTextInfo createTAGv2Sign() 	{
//		UserTextInfo userTextInfo = new UserTextInfo();
//		userTextInfo.setDescription(ID3v2_SIGN_DESCRIPTION);
//		userTextInfo.setInfo(dtf.format(tagTime));
//		return userTextInfo;
//	}
//
//	public String createTAGv1Sign() {
//		return strf("%s %s", ID3v1_SIGN_PREFIX, dtf.format(tagTime));
//	}
//
//	public LocalDateTime getTagTime() {
//		return tagTime;
//	}
}
