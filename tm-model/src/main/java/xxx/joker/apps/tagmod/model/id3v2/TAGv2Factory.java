package xxx.joker.apps.tagmod.model.id3v2;


import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2FrameFactory;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkBytes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.apps.tagmod.model.id3.standard.ID3Specs.*;

/**
 * Created by f.barbano on 27/02/2018.
 */
public class TAGv2Factory {

	public static TAGv2 parseTAGv2(BytesScanner scanner, int startIndex) {
		// Parse ID3v2 tag header
		scanner.seek(startIndex);

		BytesScanner headerScanner = scanner.nextSubScanner(ID3v2_HEADER_LENGTH, false);
		TAGv2Impl tag = parseHeaderFooter(headerScanner, true);
		if(tag == null) 		return null;

		if(tag.isCompressed())  return tag;

		BytesScanner dataScanner = scanner.nextSubScanner(tag.getSize(), false);
		parseTAGv2(tag, dataScanner);

		return tag;
	}

	public static TAGv2 parseTAGv2(RandomAccessFile raf) throws IOException {
		// Parse ID3v2 tag header
		BytesScanner scanner = BytesScanner.getScanner(raf, 0, ID3v2_HEADER_LENGTH);
		TAGv2Impl tag = parseHeaderFooter(scanner, true);
		if(tag == null) 		return null;

		if(tag.isCompressed())  return tag;

		scanner = BytesScanner.getScanner(raf, ID3v2_HEADER_LENGTH, tag.getSize());
		parseTAGv2(tag, scanner);

		return tag;
	}

	private static void parseTAGv2(TAGv2Impl tag, BytesScanner scanner) {
		// Parse ID3v2 extended header
		if(tag.isExtendedHeader()) {
			int extLen = getExtendedHeaderLength(scanner, tag.getVersion());
			tag.setExtendedHeaderLength(extLen);
			scanner.skip(extLen);
		}

		// Parse frames
		int trailZeros = scanner.remaining() - (scanner.findLastDifferentByte(0) + 1);
		int minFrameSize = ID3Specs.id3v2FrameHeaderLength(tag.getVersion()) + 1;
		int max = Math.max(minFrameSize, trailZeros);

		List<ID3v2Frame> frameList = new ArrayList<>();
		while (scanner.remaining() > max) {
			ID3v2Frame frame = ID3v2FrameFactory.parseFrame(tag.getVersion(), scanner, tag.isUnsynch());
			if (frame == null) {
				break;
			}
			frameList.add(frame);
		}
		tag.setFrameList(frameList);

		// Set padding
		tag.setPadding(scanner.remaining());

		/**
		 * REVIEW
		 * Footer not analyzed now.
		 * Analyze to check ID3 standard
 		 */
	}

	private static TAGv2Impl parseHeaderFooter(BytesScanner scanner, boolean isHeader) {
		String heading = scanner.nextString(3);
		String expectedHeading = isHeader ? ID3v2_HEADER_HEADING : ID3v2_FOOTER_HEADING;
		if (!expectedHeading.equals(heading)) {
			return null;
		}

		TAGv2Impl tag = new TAGv2Impl();

		int version = scanner.nextByteInt();
		tag.setVersion(version);
		tag.setRevision(scanner.nextByteInt());

		byte b = scanner.nextByte();
		tag.setUnsynch(JkBytes.isBitSet(b, 7));
		tag.setCompressed(JkBytes.isBitSet(b, 6) && version == 2);
		tag.setExtendedHeader(JkBytes.isBitSet(b, 6) && version != 2);
		tag.setExperimental(JkBytes.isBitSet(b, 5) && version != 2);
		tag.setFooter(JkBytes.isBitSet(b, 4) && version == 4);

		int size = TmFormat.compute32BitSynchsafeSize(scanner.nextBytes(4));
		tag.setSize(size);

		return tag;
	}

	private static int getExtendedHeaderLength(BytesScanner scanner, int version) {
		byte[] sizeBytes = scanner.getBytes(0, 4);
		int wholeSize;

		if(version == 3)	wholeSize = TmFormat.compute32BitSize(sizeBytes) + 4;
		else				wholeSize = TmFormat.compute32BitSynchsafeSize(sizeBytes);

		return wholeSize;
	}

}
