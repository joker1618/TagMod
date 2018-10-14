package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;


import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;

/**
 * Created by f.barbano on 27/02/2018.
 */
interface FrameAdapter {

	IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes);

	byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData);

}
