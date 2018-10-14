package xxx.joker.apps.tagmod.model.id3v2.frame.adapters;


import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.RawData;

/**
 * Created by f.barbano on 26/02/2018.
 */
class FrameRaw implements FrameAdapter {

	@Override
	public IFrameData parseData(int version, TxtEncoding encoding, byte[] bytes) {
		return new RawData(bytes);
	}

	@Override
	public byte[] getDataBytes(int version, TxtEncoding encoding, IFrameData frameData) {
		return ((RawData)frameData).getData();
	}

}
