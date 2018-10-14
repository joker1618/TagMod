package xxx.joker.apps.tagmod.model.id3v2.frame.enums;

import java.util.function.BiPredicate;

/**
 * Created by f.barbano on 26/02/2018.
 */
public enum FrameType {

	TEXT_INFO(
		true,
		(id, version) -> {
			String excluded = FrameName.TXXX.getFrameId(version);
			String pattern = version == 2 ? "T[A-Z0-9]{2}" : "T[A-Z0-9]{3}";
			return !id.equals(excluded) && id.matches(pattern);
		}
	),

	ATTACHED_PICTURE(true, FrameName.APIC),

	UNSYNCRONISED_LYRICS(true, FrameName.USLT),

	COMMENTS(true, FrameName.COMM),

//	PRIVATE(FrameName.PRIV),
//
//	SEEK(FrameName.SEEK),
//
	USER_TEXT_INFO(true, FrameName.TXXX),
//
//	URL_LINK(
//				(id, version) -> {
//					String excluded = FrameName.WXXX.getFrameId(version);
//					String pattern = version == 2 ? "W[IFrameData-Z0-9]{2}" : "W[IFrameData-Z0-9]{3}";
//					return !id.equals(excluded) && id.matches(pattern);
//				}
//	),
//
//	USER_DEFINED_URL_LINK(FrameName.WXXX),

	;

	private boolean textEncoded;
	private BiPredicate<String, Integer> belongClause;

	FrameType(boolean textEncoded, FrameName frameName) {
		this.textEncoded = textEncoded;
		this.belongClause = (id, version) -> id.equals(frameName.getFrameId(version));
	}
	FrameType(boolean textEncoded, BiPredicate<String, Integer> belongClause) {
		this.textEncoded = textEncoded;
		this.belongClause = belongClause;
	}

	public boolean isTextEncoded() {
		return textEncoded;
	}

	public static FrameType getByFrameId(int version, String frameId) {
		if(frameId != null) {
			for (FrameType ft : values()) {
				if (ft.belongClause.test(frameId, version)) {
					return ft;
				}
			}
		}
		return null;
	}

}
