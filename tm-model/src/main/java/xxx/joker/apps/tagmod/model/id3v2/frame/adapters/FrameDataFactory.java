package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameType;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class FrameDataFactory {

	public static IFrameData parseFrameData(int version, FrameType frameType, TxtEncoding encoding, byte[] dataBytes) {
		return getAdapter(frameType).parseData(version, encoding, dataBytes);
	}

	public static byte[] createFrameDataBytes(int version, FrameType frameType, TxtEncoding encoding, IFrameData frameData) {
		return getAdapter(frameType).getDataBytes(version, encoding, frameData);
	}

	private static FrameAdapter getAdapter(FrameType frameType) {
		if(frameType == null) {
			return new FrameRaw();
		}
		
		switch (frameType) {
			case TEXT_INFO:			return new FrameTextInfo();
			case ATTACHED_PICTURE:	return new FrameAttachedPicture();
			case UNSYNCRONISED_LYRICS:	return new FrameLyrics();
			case COMMENTS:			return new FrameComments();
			case USER_TEXT_INFO:	return new FrameUserTextInfo();
			default:				return new FrameRaw();
		}
	}
}
