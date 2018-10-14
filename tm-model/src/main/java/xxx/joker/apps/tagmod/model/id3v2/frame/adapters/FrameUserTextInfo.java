package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.UserTextInfo;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;

/**
 * Created by f.barbano on 27/02/2018.
 */


//<editor-fold desc="Frame user defined text information">
/*
    -----------------------------------------------------------
    VERSION 2
        <Header for 'User defined text information frame', ID: "TXX">
        Text encoding       $xx
        Description         <textstring> $00 (00)
        Info				<textstring>
-----------------------------------------------------------
    VERSION 3 and 4
        <Header for 'User defined text information frame', ID: "TXXX">
        Text encoding   $xx
        Description     <text string according to encoding> $00 (00)
        Info			<text string according to encoding>
    -----------------------------------------------------------

    There may be several frames, but only one with the same description
*/
//</editor-fold>

class FrameUserTextInfo implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		UserTextInfo userInfo = new UserTextInfo();

		BytesScanner scanner = BytesScanner.getScanner(bytes);

		userInfo.setDescription(scanner.nextSeparatedString(encoding));
		scanner.skip(encoding);
		userInfo.setInfo(scanner.asString(encoding));

		return userInfo;
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		UserTextInfo userInfo = (UserTextInfo)frameData;
		ByteBuilder bb = new ByteBuilder();
		bb.add(encoding.getByteNum());
		bb.add(userInfo.getDescription(), encoding);
		bb.addStringStop(encoding);
		bb.add(userInfo.getInfo(), encoding);
		return bb.build();
	}

}
