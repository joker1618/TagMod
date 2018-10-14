package xxx.joker.apps.tagmod.model.id3.standard;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;

import java.util.Arrays;
import java.util.List;

/**
 * Created by f.barbano on 01/05/2018.
 */
public class ID3Specs {

	public static final TxtEncoding DEFAULT_ENCODING = TxtEncoding.ISO_8859_1;

	public static final String ID3v1_HEADING = "TAG";
	public static final int ID3v1_TAG_LENGTH = 128;

	public static final List<Integer> ID3v2_SUPPORTED_VERSIONS = Arrays.asList(2, 3, 4);

	public static final String ID3v2_HEADER_HEADING = "ID3";
	public static final int ID3v2_HEADER_LENGTH = 10;

	public static final String ID3v2_FOOTER_HEADING = "3DI";
	public static final int ID3v2_FOOTER_LENGTH = 10;

	public static int id3v2FrameHeaderLength(int version) {
		return version == 2 ? 6 : 10;
	}

	public static boolean isValidID3v2FrameId(int version, String frameId) {
		String pattern = "[A-Z][A-Z0-9]" + (version == 2 ? "{2}" : "{3}");
		return frameId.matches(pattern);
	}

}
