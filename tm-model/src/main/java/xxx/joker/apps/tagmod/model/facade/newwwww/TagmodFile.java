package xxx.joker.apps.tagmod.model.facade.newwwww;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.apps.tagmod.model.mp3.MP3FileFactory;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TagmodFile {

	private MP3File mp3File;

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

    private void extractMP3Attributes() {
	    for(int tagNum = 0; tagNum < mp3File.getTAGv2List().size(); tagNum++) {
            TAGv2 tagv2 = mp3File.getTAGv2List().get(tagNum);
            for(ID3v2Frame frame : tagv2.getFrameList()) {
                MP3Attribute attrib = MP3Attribute.getFromFrame(frame);
                if(attrib == null) {
                    unmanagedFrames.add(frame);
                } else {
                    List<Pair<MP3Attribute, ID3v2Frame>> pairs = JkStreams.filter(attributeFrames, p -> p.getKey() == attrib);
                    if(!attrib.isMultiValue() && !pairs.isEmpty()) {
                        unmanagedFrames.add(frame);
                    } else {
                        boolean dup = !JkStreams.filter(pairs, p -> p.getValue().isFrameDuplicated(frame)).isEmpty();
                        if(dup) {
                            unmanagedFrames.add(frame);
                        } else {
                            attributeFrames.add(Pair.of(attrib, frame));
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
