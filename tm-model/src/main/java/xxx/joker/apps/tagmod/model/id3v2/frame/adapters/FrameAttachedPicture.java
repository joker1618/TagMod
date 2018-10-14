package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.PictureType;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;

/**
 * Created by f.barbano on 27/02/2018.
 */


//<editor-fold desc="Frame attached picture structure">
/*
    -----------------------------------------------------------
    VERSION 2
        <Header for 'Attached picture', ID: "PIC">
        Text encoding       $xx
        MIME type           $xx xx xx      (PNG or JPG)
        Picture type        $xx
        Description         <textstring> $00 (00)
        Picture data        <picture data>
    -----------------------------------------------------------
    VERSION 3 and 4
        <Header for 'Attached picture', ID: "APIC">
        Text encoding   $xx
        MIME type       <text string> $00   ("image/png" [PNG] or "image/jpeg" [JFIF])
        Picture type    $xx
        Description     <text string according to encoding> $00 (00)
        Picture data    <picture data>
    -----------------------------------------------------------

    There may be several pictures attached to one file, but only one with the same content descriptor
    There may only be one picture with the picture type declared as picture type $01 and $02 respectively
*/
//</editor-fold>

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

class FrameAttachedPicture implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		Picture picture = new Picture();

		BytesScanner scanner = BytesScanner.getScanner(bytes);

		String strMimeType;
		if(version == 2) {
			strMimeType = scanner.nextString(3);
		} else {
			strMimeType = scanner.nextSeparatedString(0x00);
			scanner.skip(1);
		}
		picture.setMimeType(MimeType.getByMimeType(strMimeType));

		picture.setPicType(PictureType.getByNumber(scanner.nextByteInt()));
		picture.setDescription(scanner.nextSeparatedString(encoding));
		scanner.skip(encoding);
		picture.setPicData(scanner.nextBytes());

		return picture;
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		Picture picture = (Picture) frameData;
		ByteBuilder bb = new ByteBuilder();
		bb.add(encoding.getByteNum());
		bb.add(picture.getMimeType().mimeType(version));
		if(version != 2) {
			bb.add(0x00);
		}
		bb.add(picture.getPicType().pictureNumber());
		bb.add(picture.getDescription(), encoding);
		bb.addStringStop(encoding);
		bb.add(picture.getPicData());
		return bb.build();
	}

}
