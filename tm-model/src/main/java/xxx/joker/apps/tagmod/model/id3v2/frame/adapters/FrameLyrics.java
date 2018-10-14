package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.libs.javalibs.language.JkLanguage;

/**
 * Created by f.barbano on 27/02/2018.
 */

//<editor-fold desc="Frame unsychronised lyrics structure">
/*
    -----------------------------------------------------------
    VERSION 2
        <Header for 'Unsychronised lyrics', ID: "ULT">
        Text encoding       $xx
        JkLanguage            $xx xx xx
        Description         <textstring> $00 (00)
        Lyrics              <textstring>
-----------------------------------------------------------
    VERSION 3 and 4
        <Header for 'Unsychronised lyrics', ID: "USLT">
        Text encoding   $xx
        JkLanguage        $xx xx xx
        Description     <text string according to encoding> $00 (00)
        Lyrics          <text string according to encoding>
    -----------------------------------------------------------

    There may be several frames, but with different JkLanguage and content descriptor
*/
//</editor-fold>

class FrameLyrics implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		Lyrics lyrics = new Lyrics();

		BytesScanner scanner = BytesScanner.getScanner(bytes);

		String strLanguage = scanner.nextString(3);
		lyrics.setLanguage(JkLanguage.getByLabel(strLanguage));
		lyrics.setDescription(scanner.nextSeparatedString(encoding));
		scanner.skip(encoding);
		lyrics.setText(scanner.asString(encoding));

		return lyrics;
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		Lyrics lyrics = (Lyrics) frameData;
		ByteBuilder bb = new ByteBuilder();
		bb.add(encoding.getByteNum());
		bb.add(lyrics.getLanguage().getLabel());
		bb.add(lyrics.getDescription(), encoding);
		bb.addStringStop(encoding);
		bb.add(lyrics.getText(), encoding);
		return bb.build();
	}

}
