package xxx.joker.apps.tagmod.model.id3.enums;


import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.util.TmFormat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkStrings.strf;

/**
 * Created by f.barbano on 17/01/2018.
 */

//<editor-fold desc="Picture type">
/*
    Picture types:
        $00 Other
        $01 32x32 pixels 'file icon' (PNG only)
        $02 Other file icon
        $03 Picture (front)
        $04 Picture (back)
        $05 Leaflet page
        $06 Media (e.g. lable side of CD)
        $07 Lead artist/lead performer/soloist
        $08 Artist/performer
        $09 Conductor
        $0A Band/Orchestra
        $0B Composer
        $0C Lyricist/text writer
        $0D Recording Location
        $0E During recording
        $0F During performance
        $10 Movie/video screen capture
        $11 A bright coloured fish
        $12 Illustration
        $13 Band/artist logotype
        $14 Publisher/Studio logotype
*/
//</editor-fold>

public enum PictureType {

	OTHER(0),
	FILE_ICON(1),
	OTHER_FILE_ICON(2),
	COVER_FRONT(3),
	COVER_BACK(4),
	LEAFLET(5),
	MEDIA(6),
	LEAD_ARTIST(7),
	ARTIST(8),
	CONDUCTOR(9),
	BAND(10),
	COMPOSER(11),
	LYRICIST(12),
	RECORDING_LOCATION(13),
	DURING_RECORDING(14),
	DURING_PERFORMANCE(15),
	VIDEO_SCREEN_CAPTURE(16),
	BRIGHT_COLOURED_FISHER(17),
	ILLUSTRATION(18),
	BAND_LOGOTYPE(19),
	PUBLISHER_LOGOTYPE(20),
	;

	private int pictureNumber;

	PictureType(int pictureNumber) {
		this.pictureNumber = pictureNumber;
	}

	public int pictureNumber() {
		return pictureNumber;
	}

	public String normalizedName() {
		String lowName = name().replace("_", " ").toLowerCase();
		return StringUtils.capitalize(lowName);
	}

	public static PictureType getByNumber(int picNum) {
		Optional<PictureType> picType = Arrays.stream(values()).filter(pt -> pt.pictureNumber == picNum).findFirst();
		return picType.orElse(null);
	}
	public static PictureType getByNumber(byte picNum) {
		return getByNumber(TmFormat.toInt(picNum));
	}

	@Override
	public String toString() {
		return strf("%c%s (%d)",
			name().charAt(0),
			name().substring(1).toLowerCase().replace('_', ' '),
			pictureNumber
		);
	}

	public static PictureType[] valuesNameOrdered() {
		List<PictureType> collect = Arrays.stream(values()).sorted(Comparator.comparing(PictureType::name)).collect(Collectors.toList());
		return collect.toArray(new PictureType[0]);
	}
}
