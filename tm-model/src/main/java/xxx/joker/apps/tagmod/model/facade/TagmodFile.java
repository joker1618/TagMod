package xxx.joker.apps.tagmod.model.facade;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1Impl;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Builder;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.TextInfo;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.apps.tagmod.model.mp3.MP3FileFactory;
import xxx.joker.apps.tagmod.model.struct.FPos;
import xxx.joker.libs.javalibs.utils.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static xxx.joker.apps.tagmod.model.mp3.MP3Attribute.*;

public class TagmodFile {

	private MP3File mp3File;

	private TagmodSign tagmodSign;

	private List<Pair<MP3Attribute,ID3v2Frame>> attributeFrames;
	private List<ID3v2Frame> unmanagedFrames;   // frames not used in MP3Attribute and duplicated frames

	public TagmodFile(Path mp3FilePath) throws IOException {
	    mp3File = MP3FileFactory.parse(mp3FilePath);
        attributeFrames = new ArrayList<>();
        unmanagedFrames = new ArrayList<>();
        extractMP3Attributes();
	}

    public TagmodAttributes getTagmodAttributes() {
        TagmodAttributes tmAttrib = new TagmodAttributes();
        // Add from ID3v2 tags
        attributeFrames.forEach(p -> tmAttrib.addAttribute(p.getKey(), p.getValue().getFrameData()));
        // Add missing attributes from ID3v1 tag
        MP3Attribute.orderedValues().forEach(a -> tmAttrib.addAttributeString(a, getAttributeFromTAGv1(a)));
        return tmAttrib;
    }

    public MP3File getMp3File() {
        return mp3File;
    }

    public TagmodSign getTagmodSign() {
        return tagmodSign;
    }

    public boolean isTagmodSignValid() {
        return tagmodSign != null && tagmodSign.isValid();
    }

    public boolean persistChanges(TagmodAttributes newAttribs, int version, TxtEncoding encoding, boolean unsynchronized, Integer padding, boolean signed) throws Exception {
        Map<MP3Attribute, List<IFrameData>> attrMap = newAttribs.getAttributesDataMap();

        byte[] tagv2Bytes;
        byte[] signBytes;
        byte[] tagv1Bytes;

        if(attrMap.isEmpty()) {
            if(mp3File.getFileSize() == mp3File.getSongDataFPos().getLength()) {
                // No changes found, skip persist phase
                return false;
            }
            tagv2Bytes = new byte[0];
            signBytes = new byte[0];
            tagv1Bytes = new byte[0];

        } else {
            TAGv2Builder tb = new TAGv2Builder();
            attrMap.forEach((k, v) ->
                    v.forEach(val -> tb.addFrameData(k.getFrameName(version), val))
            );
            tb.addFrameData(FrameName.COMM, TagmodSign.getID3v2FrameComment());
            tagv2Bytes = tb.buildBytes(version, encoding, unsynchronized, padding);
            tagv1Bytes = createTAGv1(newAttribs, signed).toBytes();

            if(signed) {
                byte[] tagsBytes = JkBytes.mergeArrays(tagv2Bytes, tagv1Bytes);
                String newMD5 = JkEncryption.getMD5(tagsBytes);
                if (isTagmodSignValid() && newMD5.equals(tagmodSign.getMd5hash())) {
                    // No changes found, skip persist phase
                    return false;
                }
                TagmodSign sign = TagmodSign.create(newMD5, version, encoding);
                signBytes = sign.toTAGv2Bytes();
            } else {
                signBytes = new byte[0];
            }
        }

        Path appFile = JkFiles.computeSafelyPath(mp3File.getFilePath());

        try (RandomAccessFile rafMain = new RandomAccessFile(mp3File.getFilePath().toFile(), "rw");
             RandomAccessFile rafApp = new RandomAccessFile(appFile.toFile(), "rw");
             FileChannel chMain = rafMain.getChannel();
             FileChannel chApp = rafApp.getChannel()) {

            long appPos = 0L;
            // ID3v2 tag
            if (tagv2Bytes.length > 0) {
                chApp.write(ByteBuffer.wrap(tagv2Bytes), appPos);
                appPos += tagv2Bytes.length;
            }

            // Signature ID3v2 tag
            if (signBytes.length > 0) {
                chApp.write(ByteBuffer.wrap(signBytes), appPos);
                appPos += signBytes.length;
            }

            // MP3 song data
            FPos spos = mp3File.getSongDataFPos();
            long startRead = spos.getBegin();
            long remaining = spos.getLength();
            while (remaining > 0) {
                long numRead = chMain.transferTo(startRead, remaining, chApp.position(appPos));
                remaining -= numRead;
                startRead += numRead;
                appPos += numRead;
            }

            // ID3v1 tag
            if (tagv1Bytes.length > 0) {
                chApp.write(ByteBuffer.wrap(tagv1Bytes), appPos);
                appPos += tagv1Bytes.length;
            }

            // Reverse bytes from app to main
            long posIndex = 0L;
            long offset = chApp.size();
            while (offset > 0) {
                long numRead = chApp.transferTo(posIndex, offset, chMain.position(posIndex));
                offset -= numRead;
                posIndex += numRead;
            }

            chMain.truncate(chApp.size());
            return true;

        } finally {
            Files.deleteIfExists(appFile);
        }
    }

    private TAGv1 createTAGv1(TagmodAttributes tmAttribs, boolean insertSign) {
        Integer trackNum = getIntAttr(tmAttribs, TRACK);
        Integer genreNum = getIntAttr(tmAttribs, GENRE);
        TAGv1 tag = new TAGv1Impl();
        tag.setRevision(trackNum != null ? 1 : 0);
        tag.setTitle(getStringAttr(tmAttribs, TITLE));
        tag.setArtist(getStringAttr(tmAttribs, ARTIST));
        tag.setAlbum(getStringAttr(tmAttribs, ALBUM));
        tag.setYear(getStringAttr(tmAttribs, YEAR));
        tag.setComments(insertSign ? TagmodSign.getID3TagmodComment() : "");
        if(trackNum != null)    tag.setTrack(trackNum);
        if(genreNum != null)    tag.setGenre(genreNum);
        return tag;
    }
    private String getStringAttr(TagmodAttributes tmAttribs, MP3Attribute attr) {
        TextInfo textInfo = tmAttribs.getFrameDataCasted(attr);
        return textInfo == null ? "" : textInfo.getInfo();
    }
    private Integer getIntAttr(TagmodAttributes tmAttribs, MP3Attribute attr) {
        TextInfo textInfo = tmAttribs.getFrameDataCasted(attr);
        if(textInfo != null) {
            if(attr == TRACK) {
                String s = textInfo.getInfo().replaceAll("/.*", "");
                return JkConverter.stringToInteger(s);
            }
            if(attr == GENRE) {
                ID3Genre genre = ID3Genre.getByName(textInfo.getInfo());
                return genre == null ? null : genre.getGenreNum();
            }
        }
        return null;
    }

    private void extractMP3Attributes() {
        tagmodSign = TagmodSign.parse(mp3File);

	    for(int tagNum = 0; tagNum < mp3File.getTAGv2List().size(); tagNum++) {
            TAGv2 tagv2 = mp3File.getTAGv2List().get(tagNum);

            if(tagmodSign == null || tagmodSign.getTagNum() != tagNum) {
                for (ID3v2Frame frame : tagv2.getFrameList()) {
                    MP3Attribute attrib = MP3Attribute.getFromFrame(frame);
                    if (attrib == null) {
                        unmanagedFrames.add(frame);
                    } else {
                        List<Pair<MP3Attribute, ID3v2Frame>> pairs = JkStreams.filter(attributeFrames, p -> p.getKey() == attrib);
                        if (!attrib.isMultiValue() && !pairs.isEmpty()) {
                            unmanagedFrames.add(frame);
                        } else {
                            boolean dup = !JkStreams.filter(pairs, p -> p.getValue().isFrameDuplicated(frame)).isEmpty();
                            if (dup) {
                                unmanagedFrames.add(frame);
                            } else {
                                attributeFrames.add(Pair.of(attrib, frame));
                            }
                        }
                    }
                }
            }
        }
    }

	private String getAttributeFromTAGv1(MP3Attribute attribute) {
		TAGv1 tagv1 = mp3File.getTAGv1();
		if(tagv1 == null) {
			return null;
		}

		switch (attribute) {
			case TITLE:		return tagv1.getTitle();
			case ARTIST:	return tagv1.getArtist();
			case ALBUM:		return tagv1.getAlbum();
			case YEAR:		return tagv1.getYear();
			case TRACK:		return tagv1.getRevision() == 1 && tagv1.getTrack() > 0 ? String.valueOf(tagv1.getTrack()) : null;
			case GENRE:
				ID3Genre g = ID3Genre.getByNumber(tagv1.getGenre());
				return g == null ? null : g.getGenreName();

			default: 	return null;
		}
	}

}
