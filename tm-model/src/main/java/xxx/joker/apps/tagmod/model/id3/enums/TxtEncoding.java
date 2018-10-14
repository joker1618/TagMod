package xxx.joker.apps.tagmod.model.id3.enums;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by f.barbano on 26/02/2018.
 */

//<editor-fold desc="Text encoding for string informations">
/*
	$00 – ISO-8859-1 (LATIN-1, Identical to ASCII for values smaller than 0x80).
	$01 – UCS-2 (UTF-16 encoded Unicode with BOM), in ID3v2.2 and ID3v2.3.
	$02 – UTF-16BE encoded Unicode without BOM, in ID3v2.4.
	$03 – UTF-8 encoded Unicode, in ID3v2.4.
*/
//</editor-fold>

public enum TxtEncoding {

	ISO_8859_1	("ISO-8859-1", 	"iso",		0x00, 	(byte)0x00),
	UTF_16		("UTF-16", 		"utf16",	0x01, 	(byte)0x00, (byte)0x00),
	UTF_16BE	("UTF-16BE", 	"utf16be",	0x02, 	(byte)0x00, (byte)0x00),
	UTF_8		("UTF-8", 		"utf8",		0x03, 	(byte)0x00),

	;

	private String charsetName;
	private String label;
	private byte byteNum;
	private byte[] stringStop;

	TxtEncoding(String charsetName, String label, int encodingByte, byte... stringStop) {
		this.charsetName = charsetName;
		this.label = label;
		this.byteNum = (byte) encodingByte;
		this.stringStop = stringStop;
	}

	public Charset getCharset() {
		return Charset.forName(charsetName);
	}

	public String getCharsetName() {
		return charsetName;
	}

	public String getLabel() {
		return label;
	}

	public byte getByteNum() {
		return byteNum;
	}

	public byte[] getStringStop() {
		return stringStop;
	}

	public boolean isVersionValid(int version) {
		return version == 4 || Arrays.asList(ISO_8859_1, UTF_16).contains(this);
	}

	public static TxtEncoding getFromByte(byte b) {
		for (TxtEncoding te : values()) {
			if (te.byteNum == b) {
				return te;
			}
		}
		return null;
	}

	public static TxtEncoding getFromLabel(String label) {
		for (TxtEncoding te : values()) {
			if (te.label.equalsIgnoreCase(label)) {
				return te;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return charsetName;
	}

	public static TxtEncoding fromLabel(String encodingLabel) {
		for(TxtEncoding enc : values()) {
			if(enc.label.equalsIgnoreCase(encodingLabel)) {
				return enc;
			}
		}
		return null;
	}
}
