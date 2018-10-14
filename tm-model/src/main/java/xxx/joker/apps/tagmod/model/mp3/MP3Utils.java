package xxx.joker.apps.tagmod.model.mp3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * Created by f.barbano on 27/12/2017.
 */
public class MP3Utils {

	// MP3 frame header start bytes
	private static final int FIRST_BYTE = 0xFF;   // exact value
	private static final int SECOND_BYTE = 0xE2;  // min value

	// number of consecutive MP3 frame headers to accept the begin of data
	private static final int CONSECUTIVE_HEADER_NUMBER = 10;


	public static int findFirstFramePosition(Path filePath, int startIndex) {
		try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
			return findFirstFramePosition(raf, startIndex);
		} catch(IOException ex) {
			return -1;
		}
	}

	public static int findFirstFramePosition(RandomAccessFile raf, int startIndex) {
		try {

			int mainStart = startIndex;
			int secondStart = startIndex;
			int mp3Size = (int) raf.length();

			int count = 0;
			int first = -1;
			int second = -1;

			while (mainStart < mp3Size) {
				if(mainStart == secondStart) {
					raf.seek(mainStart);
					first = raf.read();
					second = raf.read();
				}

				boolean found = false;

				if (first == FIRST_BYTE && second >= SECOND_BYTE) {
					raf.seek(secondStart);
					byte[] data = new byte[4];
					raf.read(data);
					MP3FrameHeader mp3FrameHeader = MP3FrameHeader.parseMP3FrameHeader(data);
					if (mp3FrameHeader != null) {
						count++;
						if (count == CONSECUTIVE_HEADER_NUMBER) {
							return mainStart;
						}
						secondStart += mp3FrameHeader.frameSize();
						found = true;
					}
				}

				if (!found) {
					mainStart++;
					secondStart = mainStart;
					count = 0;
				}
			}

			return -1;

		} catch(IOException ex) {
			return -1;
		}
	}

}
