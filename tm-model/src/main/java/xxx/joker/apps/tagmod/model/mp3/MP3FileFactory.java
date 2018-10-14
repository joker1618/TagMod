package xxx.joker.apps.tagmod.model.mp3;

import it.sauronsoftware.jave.EncoderException;
import xxx.joker.apps.tagmod.model.beans.FPos;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1Impl;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Factory;
import xxx.joker.apps.tagmod.util.BytesScanner;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.media.analysis.JkAudioInfo;
import xxx.joker.libs.javalibs.media.analysis.JkMediaAnalyzer;
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

		try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
			MP3FileImpl mp3File = parseMP3File(raf);
			mp3File.setFilePath(filePath);
			JkAudioInfo audioInfo = JkMediaAnalyzer.analyzeAudio(filePath);
			mp3File.setAudioInfo(audioInfo);
			return mp3File;
		}
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
		int songStart = MP3Utils.findFirstFramePosition(raf, tagv2Size);

		// Find for others TAGv2
		int dirtyBytes = songStart - tagv2Size;
		if(dirtyBytes > 0) {
			BytesScanner dirtyScanner = BytesScanner.getScanner(raf, tagv2Size, dirtyBytes);
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
		mp3File.setDirtyBytes(new FPos(songStart-dirtyBytes, songStart));

		// Find TAGv1
		byte[] tagv1Bytes = JkBytes.getBytes(raf, (int) raf.length() - ID3Specs.ID3v1_TAG_LENGTH, ID3Specs.ID3v1_TAG_LENGTH);
		TAGv1 tagv1 = TAGv1Impl.createFromBytes(tagv1Bytes);
		int songEnd;
		if(tagv1 == null) {
			songEnd = (int) raf.length();
		} else {
			mp3File.setTAGv1(tagv1);
			songEnd = (int) raf.length() - ID3Specs.ID3v1_TAG_LENGTH;
		}

		// Set song end
		mp3File.setSongDataFPos(new FPos(songStart, songEnd));

		return mp3File;
	}




}
