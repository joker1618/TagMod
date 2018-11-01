package xxx.joker.apps.tagmod.model.mp3;


import xxx.joker.apps.tagmod.common.TagmodConst;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by f.barbano on 17/01/2018.
 */
public enum MP3Attribute {

	TITLE           (FrameName.TIT2, 1),
	ARTIST          (FrameName.TPE1, 5),
	ALBUM           (FrameName.TALB, 10),
	YEAR            (FrameName.TYER, FrameName.TYER, FrameName.TDRC, 20),
	TRACK			(FrameName.TRCK, 30),
	GENRE           (FrameName.TCON, 40),
	CD_POS			(FrameName.TPOS, 45, false, false),
	COVER			(FrameName.APIC, 50, false, f -> ((Picture)f).getPicType() == TagmodConst.COVER_TYPE && ((Picture)f).getDescription().equals(TagmodConst.COVER_DESCR)),
	PICTURES(FrameName.APIC, 55, true, false, f -> !(((Picture)f).getPicType() == TagmodConst.COVER_TYPE && ((Picture)f).getDescription().equals(TagmodConst.COVER_DESCR))),
	LYRICS     		(FrameName.USLT, 60, false, f -> ((Lyrics)f).getDescription().equals(TagmodConst.LYRICS_DESCR)),
	OTHER_LYRICS	(FrameName.USLT, 65, true, false, f -> !((Lyrics)f).getDescription().equals(TagmodConst.LYRICS_DESCR)),

	;

	private FrameName v2;
	private FrameName v3;
	private FrameName v4;
	private int position;
	private boolean multiValue;
	private Predicate<IFrameData> acceptCond;

	MP3Attribute(FrameName frameName, int position) {
		this(frameName, position, false);
	}
	MP3Attribute(FrameName frameName, int position, boolean multiValue) {
		this(frameName, frameName, frameName, position, multiValue, f -> true, true);
	}
	MP3Attribute(FrameName frameName, int position, boolean multiValue, boolean required) {
		this(frameName, frameName, frameName, position, multiValue, f -> true, required);
	}
    MP3Attribute(FrameName frameName, int position, boolean multiValue, Predicate<IFrameData> acceptCond) {
        this(frameName, frameName, frameName, position, multiValue, acceptCond, true);
    }
    MP3Attribute(FrameName frameName, int position, boolean multiValue, boolean required, Predicate<IFrameData> acceptCond) {
        this(frameName, frameName, frameName, position, multiValue, acceptCond, required);
    }
	MP3Attribute(FrameName v2, FrameName v3, FrameName v4, int position) {
		this(v2, v3, v4, position, false);
	}
	MP3Attribute(FrameName v2, FrameName v3, FrameName v4, int position, boolean multiValue) {
		this(v2, v3, v4, position, multiValue, f -> true, true);
	}
	MP3Attribute(FrameName v2, FrameName v3, FrameName v4, int position, boolean multiValue, Predicate<IFrameData> acceptCond, boolean required) {
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.position = position;
		this.multiValue = multiValue;
		this.acceptCond = acceptCond;
	}

	public static List<MP3Attribute> orderedValues() {
		return Arrays.stream(values())
				   .sorted(Comparator.comparingInt(MP3Attribute::getPosition))
				   .collect(Collectors.toList());
	}

	public int getPosition() {
		return position;
	}

	public FrameName getFrameName(int version) {
		switch (version) {
			case 2:	return v2;
			case 3:	return v3;
			case 4:	return v4;
		}
		return null;
	}

	public boolean isMultiValue() {
		return multiValue;
	}

	public static MP3Attribute getFromFrame(ID3v2Frame frame) {
		FrameName frameName = frame.getFrameName();
		if(frameName != null) {
			for (MP3Attribute attr : values()) {
				if (attr.v2 == frameName || attr.v3 == frameName || attr.v4 == frameName) {
					if(attr.acceptCond.test(frame.getFrameData())) {
						return attr;
					}
				}
			}
		}
		return null;
	}



}
