package xxx.joker.apps.tagmod.model.mp3;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1Impl;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Factory;
import xxx.joker.apps.tagmod.model.struct.FPos;
import xxx.joker.apps.tagmod.stuff.TmcDebug;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkBytes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;


/**
 * Created by f.barbano on 27/02/2018.
 */
public class MP3FileFactory {

	public static MP3File parse(Path filePath) throws IOException {
		if(!TmFormat.isMP3File(filePath))
			return null;

        MP3FileImpl mp3File;
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
			mp3File = parseMP3File(raf);
            if(mp3File == null)     return null;
            mp3File.setFilePath(filePath);
			mp3File.setFileSize(raf.length());
		}
        TmcDebug.addTime("A");

//        JkAudioInfo audioInfo = JkMediaAnalyzer.analyzeMP3(filePath);
//        mp3File.setAudioInfo(audioInfo);
//        TmcDebug.addTime("B");

        return mp3File;
	}

	private static MP3FileImpl parseMP3File(RandomAccessFile raf) throws IOException {
		MP3FileImpl mp3File = new MP3FileImpl();

		// Parse first ID3v2 tag
		TAGv2 tagv2 = TAGv2Factory.parseTAGv2(raf);
		int tagv2Size = 0;
		if(tagv2 != null) {
			mp3File.getTAGv2List().add(tagv2);
			tagv2Size = tagv2.getTagLength();
		}

		// Find song start
        Pair<Long, MP3FrameHeader> startFrame = MP3Utils.findFirstFrame(raf, tagv2Size);
        if(startFrame == null) {
		    return null;
        }

        // Find for others TAGv2
		long dirtyBytes = startFrame.getKey() - tagv2Size;
		if(dirtyBytes > 0) {
			BytesScanner dirtyScanner = BytesScanner.getScanner(raf, tagv2Size, (int)dirtyBytes);
			while (dirtyBytes > 0) {
				TAGv2 otherTag = TAGv2Factory.parseTAGv2(dirtyScanner, dirtyScanner.position());
				if (otherTag != null) {
					mp3File.getTAGv2List().add(otherTag);
					dirtyScanner.skip(otherTag.getTagLength());
					dirtyBytes -= otherTag.getTagLength();
				} else {
					break;
				}
			}
		}
		mp3File.setDirtyBytes(new FPos(startFrame.getKey()-dirtyBytes, startFrame.getKey()));

        // Find TAGv1
		byte[] tagv1Bytes = JkBytes.getBytes(raf, (int) raf.length() - ID3Specs.ID3v1_TAG_LENGTH, ID3Specs.ID3v1_TAG_LENGTH);
		TAGv1 tagv1 = TAGv1Impl.createFromBytes(tagv1Bytes);
		long songEnd;
		if(tagv1 == null) {
			songEnd = raf.length();
		} else {
			mp3File.setTAGv1(tagv1);
			songEnd = raf.length() - ID3Specs.ID3v1_TAG_LENGTH;
		}

		// Set song end
        FPos songFPos = new FPos(startFrame.getKey(), songEnd);
        mp3File.setSongDataFPos(songFPos);

        // Set song length
        mp3File.setAudioInfo(new MP3AudioInfo(songFPos.getLength(), startFrame.getValue()));
        return mp3File;
	}




}
