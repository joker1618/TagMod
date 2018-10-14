package xxx.joker.apps.tagmod.console.workers;

import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.format.JkOutputFmt;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;
import xxx.joker.libs.javalibs.utils.JkStrings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcViewer {

	public static final String LEFT_PAD_PREFIX = "  ";
	public static final int COLUMNS_DISTANCE = 3;
	public static final String NO_VALUE = "-";


	public static String toStringMP3Attributes(TagmodFile tmFile) {
		List<String> lines = new ArrayList<>();

		for(MP3Attribute attr : MP3Attribute.orderedValues()) {
			if(attr == MP3Attribute.COVER) {
				lines.add(strf("%s:;%s", attr, tmFile.getCover() == null ? NO_VALUE : tmFile.getCover().toStringInline()));
			} else if(attr == MP3Attribute.PICTURE) {
				tmFile.getPictures().forEach(c -> lines.add(strf("%s:;%s", attr, c.toStringInline())));
			} else if(attr == MP3Attribute.LYRICS) {
				lines.add(strf("%s:;%s", attr, tmFile.getLyrics() == null ? NO_VALUE : tmFile.getLyrics().toStringInline()));
			} else if(attr == MP3Attribute.OTHER_LYRICS) {
				tmFile.getOtherLyrics().forEach(l -> lines.add(strf("%s:;%s", attr, l.toStringInline())));
			} else {  // text info attribute
				lines.add(strf("%s:;%s", attr, tmFile.getTextInfoAttribute(attr) == null ? NO_VALUE : tmFile.getTextInfoAttribute(attr).toStringInline()));
			}
		}

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		outb.addLines(lines);
		outb.insertPrefix(LEFT_PAD_PREFIX);
		String attrString = outb.toString(";", COLUMNS_DISTANCE);

		return "MP3 ATTRIBUTES\n" + attrString;
	}

	public static String describe(TagmodFile tmFile) {
		List<String> lines = new ArrayList<>();
		lines.add(toStringMainDetails(tmFile));
		tmFile.getMp3File().getTAGv2List().forEach(tag -> lines.add(toStringTAGv2(tag)));
		lines.add(toStringTAGv1(tmFile.getMp3File().getTAGv1()));
		return JkStreams.join(lines, StringUtils.LF);
	}

	public static String toStringMainDetails(TagmodFile tmFile) {
		MP3File mp3File = tmFile.getMp3File();
		long counter = 0L;
		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();

		long fsize = JkFiles.safeSize(mp3File.getFilePath());
		outb.addLines(strf("Size;%d;%s", fsize, JkOutputFmt.humanSize(fsize)));

		if(mp3File.getTAGv2List().isEmpty()) {
			outb.addLines("ID3v2;NO;0");
		} else {
			for(TAGv2 tag : mp3File.getTAGv2List()) {
				counter += tag.getTagLength();
				outb.addLines(strf("ID3v2.%d;%d;%d", tag.getVersion(), tag.getTagLength(), counter));
			}
		}

		int dirtyLen = mp3File.getDirtyBytes().getLength();
		counter += dirtyLen;
		outb.addLines(strf("Dirty bytes;%d;%d", dirtyLen, counter));

		int songLen = mp3File.getSongDataFPos().getLength();
		counter += songLen;
		outb.addLines(strf("Song data;%d;%d", songLen, counter));

		if(mp3File.getTAGv1() == null) {
			outb.addLines(strf("ID3v1;NO;%d", counter));
		} else {
			counter += mp3File.getTAGv1().getTagLength();
			outb.addLines(strf("ID3v1.%d;%d;%d", mp3File.getTAGv1().getRevision(), mp3File.getTAGv1().getTagLength(), counter));
		}

		outb.insertPrefix(LEFT_PAD_PREFIX);
		outb.setColumnAlign(TextAlignment.RIGHT, 1, 2);
		String strDetails = outb.toString(";", COLUMNS_DISTANCE);

		return "MAIN DETAILS\n" + strDetails;
	}

	public static String toStringTAGv2(TagmodFile tmFile) {
		List<TAGv2> tags = tmFile.getMp3File().getTAGv2List();
		return tags.isEmpty() ? "TAGv2  ->  NO" : JkStreams.join(tags, StringUtils.LF, TmcViewer::toStringTAGv2);
	}
	public static String toStringTAGv2(TAGv2 tag) {
		if(tag == null)	return "TAGv2  ->  NO";

		JkColumnFmtBuilder outbFrames = new JkColumnFmtBuilder();
		List<ID3v2Frame> sorted = tag.getFrameList().stream().sorted(Comparator.comparing(ID3v2Frame::getFrameId)).collect(Collectors.toList());
		for(ID3v2Frame f : sorted) {
			outbFrames.addLines(strf("%s;%s;%s", f.getFrameId(), f.getEncoding() != null ? f.getEncoding().getLabel() : NO_VALUE, f.getFrameData().toStringInline()));
		}
		outbFrames.insertPrefix(LEFT_PAD_PREFIX+LEFT_PAD_PREFIX);
		String strFrames = outbFrames.toString(";", COLUMNS_DISTANCE);

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		List<String> flags = new ArrayList<>();
		if(tag.isUnsynch())			flags.add("unsynch");
		if(tag.isCompressed())		flags.add("compressed");
		if(tag.isExtendedHeader())	flags.add("ext_header");
		if(tag.isExperimental())	flags.add("experimental");
		if(tag.isFooter())			flags.add("footer");
		if(!flags.isEmpty()) {
			outb.addLines(strf("Flags:;%s", JkStreams.join(flags, ", ")));
		}

		outb.addLines(strf("Size:;%s  (%d)", JkOutputFmt.humanSize(tag.getTagLength()), tag.getTagLength()));
		outb.addLines(strf("Padding:;%d", tag.getPadding()));
		outb.addLines(strf("Frames:;%d", tag.getFrameList().size()));

		outb.insertPrefix(LEFT_PAD_PREFIX);
		String strMain = outb.toString(";", COLUMNS_DISTANCE);

		return strf("TAGv2.%d\n%s\n%s", tag.getVersion(), strMain, strFrames);
	}

	public static String toStringTAGv1(TagmodFile tmFile) {
		return toStringTAGv1(tmFile.getMp3File().getTAGv1());
	}
	public static String toStringTAGv1(TAGv1 tag) {
		if(tag == null)	return "TAGv1  ->  NO";

		ID3Genre genre = ID3Genre.getByNumber(tag.getGenre());

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		outb.addLines(strf("Title:;%s", tag.getTitle()));
		outb.addLines(strf("Artist:;%s", tag.getArtist()));
		outb.addLines(strf("Album:;%s", tag.getAlbum()));
		outb.addLines(strf("Year:;%s", tag.getYear()));
		outb.addLines(strf("Comments:;%s", tag.getComments()));
		outb.addLines(strf("Track:;%s", tag.getRevision() == 1 ? String.valueOf(tag.getTrack()) : NO_VALUE));
		outb.addLines(strf("Genre:;%s", genre == null ? NO_VALUE : strf("%s (%d)", genre.name(), genre.getGenreNum())));
		outb.insertPrefix(LEFT_PAD_PREFIX);

		return strf("TAGv1.%d\n%s", tag.getRevision(), outb.toString(";", COLUMNS_DISTANCE));
	}

	public static List<String> toStringLyrics(TagmodFile tmFile) {
		List<Lyrics> lyricsList = new ArrayList<>();
		if(tmFile.getLyrics() != null) {
		    lyricsList.add(tmFile.getLyrics());
        }
		lyricsList.addAll(tmFile.getOtherLyrics());

		List<String> toRet = new ArrayList<>();

		for(Lyrics lyrics : lyricsList) {
			List<String> lines = new ArrayList<>();
			String strLyricsDescr = "Lyrics description:  ";
			String strContent = strf("%s*%s* (%s)", strLyricsDescr, lyrics.getDescription(), lyrics.getLanguage().getLabel().toUpperCase());
			lines.add(strContent);

			lines.add("Lyrics text:");
			List<String> textLines = JkStrings.splitFieldsList(lyrics.getText().trim(), StringUtils.LF, true);
			int width = textLines.stream().mapToInt(String::length).max().orElse(0);
			textLines = JkStreams.map(textLines, line -> StringUtils.center(line, width));
			textLines = JkStrings.leftPadLines(textLines, " ", strLyricsDescr.length());
			lines.addAll(textLines);

			toRet.add(JkStreams.join(lines, StringUtils.LF));
		}

		return toRet;
	}

}