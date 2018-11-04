package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Comments;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.libs.language.JkLanguage;

/**
 * Created by f.barbano on 27/02/2018.
 */


//<editor-fold desc="Frame comments structure">
/*
    -----------------------------------------------------------
    VERSION 2
        <Header for 'Comments', ID: 'COM'>
        Text encoding       $xx
        JkLanguage            $xx xx xx
        Description         <textstring> $00 (00)
        Text				<textstring>
-----------------------------------------------------------
    VERSION 3 and 4
        <Header for 'Comments', ID: 'COMM">
        Text encoding   $xx
        JkLanguage        $xx xx xx
        Description     <text string according to encoding> $00 (00)
        Text			<text string according to encoding>
    -----------------------------------------------------------

    There may be several frames, but with different JkLanguage and content descriptor
*/
//</editor-fold>

class FrameComments implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		BytesScanner scanner = BytesScanner.getScanner(bytes);

		Comments comm = new Comments();
		String strLanguage = scanner.nextString(3);
        JkLanguage lan = JkLanguage.getByLabel(strLanguage);
        lan = lan == null ? JkLanguage.ENGLISH : lan;
        comm.setLanguage(lan);
		comm.setDescription(scanner.nextSeparatedString(encoding));
		scanner.skip(encoding);
		comm.setText(scanner.asString(encoding));

		return comm;
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		Comments comm = (Comments) frameData;
		ByteBuilder bb = new ByteBuilder();
		bb.add(encoding.getByteNum());
		bb.add(comm.getLanguage().getLabel());
		bb.add(comm.getDescription(), encoding);
		bb.addStringStop(encoding);
		bb.add(comm.getText(), encoding);
		return bb.build();
	}

}
