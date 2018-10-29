package xxx.joker.apps.tagmod.model.id3v2.frame;


import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v2.frame.adapters.FrameDataFactory;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameType;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkBytes;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class ID3v2FrameFactory {

	public static ID3v2Frame parseFrame(int version, BytesScanner scanner, boolean isTagUnsynch) {
		int headerLength = ID3Specs.id3v2FrameHeaderLength(version);
		byte[] headerBytes = scanner.getBytes(0, headerLength);

		ID3v2FrameImpl frame = parseHeader(version, headerBytes, isTagUnsynch);
		if(frame == null) {
			return null;
		}

		scanner.skip(headerLength);

		BytesScanner dataScanner = scanner.nextSubScanner(frame.getSize(), frame.isUnsynch());
		FrameType ftype = frame.getFrameType();

		if(ftype != null && ftype.isTextEncoded()) {
			frame.setEncoding(TxtEncoding.getFromByte(dataScanner.nextByte()));
		}

		IFrameData frameData = FrameDataFactory.parseFrameData(version, ftype, frame.getEncoding(), dataScanner.nextBytes());
		frame.setFrameData(frameData);

		return frame;
	}
	private static ID3v2FrameImpl parseHeader(int version, byte[] bytes, boolean isTagUnsynch) {
		BytesScanner scanner = BytesScanner.getScanner(bytes);

		// Parse header
		int len = version == 2 ? 3 : 4;
		String frameId = scanner.getString(0, len);
		if(!ID3Specs.isValidID3v2FrameId(version, frameId)) {
			return null;
		}

		scanner.skip(len);

		ID3v2FrameImpl frame = new ID3v2FrameImpl(version, frameId);

		boolean unsynch = isTagUnsynch;
		int size;
		if(version == 2) {
			size = TmFormat.compute24BitSize(scanner.nextBytes(3));
		} else if (version == 3) {
			size = TmFormat.compute32BitSize(scanner.nextBytes(4));
			scanner.skip(2);
		} else {	// version 4
			size = TmFormat.compute32BitSynchsafeSize(scanner.nextBytes(4));
			byte[] arr = scanner.nextBytes(2);
			unsynch = JkBytes.isBitSet(arr[1], 1);
		}

		frame.setUnsynch(unsynch);
		frame.setSize(size);

		return frame;
	}

	public static byte[] createFrameBytes(int version, FrameName frameName, TxtEncoding encoding, IFrameData frameData, boolean unsynch) {
		return createFrameBytes(version, frameName.getFrameId(version), encoding, frameData, unsynch);
	}
	public static byte[] createFrameBytes(int version, String frameId, TxtEncoding encoding, IFrameData frameData, boolean unsynch) {
		FrameType frameType = FrameType.getByFrameId(version, frameId);
		byte[] dataBytes = FrameDataFactory.createFrameDataBytes(version, frameType, encoding, frameData);
		int size = dataBytes.length;

		ByteBuilder bb = new ByteBuilder();
		bb.add(frameId);

		if(version == 2) {
			bb.add(TmFormat.numberTo24Bit(size));
		} else if (version == 3) {
			bb.add(TmFormat.numberTo32Bit(size));
			bb.add(0x00);
			bb.add(0x00);
		} else if (version == 4) {
			bb.add(TmFormat.numberTo32BitSynchsafe(size));
			bb.add(0x00);
			bb.add(unsynch ? JkBytes.setBit(1) : 0x00);
		}

		byte[] arr = unsynch ? TmFormat.addUnsynchronisation(dataBytes) : dataBytes;
		bb.add(arr);

		return bb.build();
	}
	public static byte[] createFrameBytes(ID3v2Frame frame) {
	    return createFrameBytes(frame.getVersion(), frame.getFrameId(), frame.getEncoding(), frame.getFrameData(), frame.isUnsynch());
	}
}
