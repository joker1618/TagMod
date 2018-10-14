package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.TextInfo;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.TmFormat;

/**
 * Created by f.barbano on 27/02/2018.
 */

//<editor-fold desc="Frame text info structure">
/*
    -----------------------------------------------------------
    VERSION 2
        <Header for 'Text information frame', ID: "T00" - "TZZ" , excluding "TXX">
        Text encoding   $xx
        Information     <textstring>
    -----------------------------------------------------------
    VERSION 3 and 4
        <Header for 'Text information frame', ID: "T000" - "TZZZ", excluding "TXXX">
        Text encoding   $xx
        Information     <text string(s) according to encoding>
    -----------------------------------------------------------

    Every frame ID can appear once!
*/
//</editor-fold>

class FrameTextInfo implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		String info = TmFormat.toString(bytes, encoding);
		return new TextInfo(info);
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		String info = ((TextInfo) frameData).getInfo();
		return new ByteBuilder()
				   .add(encoding.getByteNum())
				   .add(info, encoding)
				   .build();
	}

}
