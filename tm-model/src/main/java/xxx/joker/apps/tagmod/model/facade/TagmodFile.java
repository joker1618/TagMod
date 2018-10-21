package xxx.joker.apps.tagmod.model.facade;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.beans.TmSignature;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1Impl;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Factory;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2FrameFactory;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.TextInfo;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.apps.tagmod.model.mp3.MP3FileFactory;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.libs.javalibs.utils.JkBytes;
import xxx.joker.libs.javalibs.utils.JkConverter;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static xxx.joker.apps.tagmod.model.mp3.MP3Attribute.*;

public class TagmodFile {

	private MP3File mp3File;

	private Map<MP3Attribute,TextInfo> textInfoAttrs;
	private Picture cover;
	private List<Picture> pictures;
	private Lyrics lyrics;
	private List<Lyrics> otherLyrics;

	public TagmodFile(Path mp3FilePath) throws IOException {
		this.mp3File = MP3FileFactory.parse(mp3FilePath);
		this.textInfoAttrs = new TreeMap<>(Comparator.comparing(MP3Attribute::getPosition));
		this.pictures = new ArrayList<>();
		this.otherLyrics = new ArrayList<>();
		extractMP3Attributes();
	}

	public void clearAllAttributes() {
	    textInfoAttrs.clear();
	    cover = null;
	    pictures.clear();
	    lyrics = null;
	    otherLyrics.clear();
    }

	public MP3File getMp3File() {
		return mp3File;
	}
	public TextInfo getTextInfoAttribute(MP3Attribute attr) {
		return textInfoAttrs.get(attr);
	}
	public Picture getCover() {
		return cover;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public Lyrics getLyrics() {
		return lyrics;
	}
	public List<Lyrics> getOtherLyrics() {
		return otherLyrics;
	}

	public void setTextInfoAttribute(MP3Attribute attr, String info) {
		this.textInfoAttrs.put(attr, new TextInfo(info));
	}
	public void setCover(Picture cover) {
		this.cover = cover;
	}
	public void setLyrics(Lyrics lyrics) {
		this.lyrics = lyrics;
	}

	public void deleteTextInfoAttribute(MP3Attribute attr) {
		textInfoAttrs.remove(attr);
	}
	public void deleteCover() {
		cover = null;
	}
	public void deletePictures() {
		pictures.clear();
	}
	public void deleteLyrics() {
		lyrics = null;
	}
	public void deleteOtherLyrics() {
		otherLyrics.clear();
	}

    public byte[] toBytes(int version, TxtEncoding encoding, boolean unsynch, int padding) throws IOException {
		// Frames
		List<byte[]> fblist = new ArrayList<>();

		textInfoAttrs.forEach(
			(attr,info) -> fblist.add(ID3v2FrameFactory.createFrameBytes(version, attr.getFrameName(version), encoding, info, unsynch))
		);
		if(cover != null){
			fblist.add(ID3v2FrameFactory.createFrameBytes(version, COVER.getFrameName(version), encoding, cover, unsynch));
		}
		if(lyrics != null){
			fblist.add(ID3v2FrameFactory.createFrameBytes(version, LYRICS.getFrameName(version), encoding, lyrics, unsynch));
		}
		pictures.forEach(pic ->
			fblist.add(ID3v2FrameFactory.createFrameBytes(version, FrameName.APIC, encoding, pic, unsynch))
		);
		otherLyrics.forEach(lyr ->
			fblist.add(ID3v2FrameFactory.createFrameBytes(version, FrameName.USLT, encoding, lyr, unsynch))
		);

		// TAGv2
		ByteBuilder bb = new ByteBuilder();
		bb.add(TAGv2Factory.createTAGv2(version, fblist, unsynch, padding));

		// Song data
		bb.add(JkBytes.getBytes(mp3File.getFilePath(), mp3File.getSongDataFPos().getBegin(), mp3File.getSongDataFPos().getLength()));

		// TAGv1
		bb.add(createTAGv1().toBytes());

		return bb.build();
	}

	private TAGv1 createTAGv1() {
		TAGv1 tag = new TAGv1Impl();
		tag.setRevision(1);
		tag.setTitle(getStringAttr(TITLE));
		tag.setArtist(getStringAttr(ARTIST));
		tag.setAlbum(getStringAttr(ALBUM));
		tag.setYear(getStringAttr(YEAR));
		tag.setComments("");
		tag.setTrack(getTrackAttr());
		tag.setGenre(getGenreAttr());
		return tag;
	}
	private String getStringAttr(MP3Attribute attr) {
		TextInfo ti = textInfoAttrs.get(attr);
		return ti == null ? "" : ti.getInfo();
	}
	private int getTrackAttr() {
		TextInfo ti = textInfoAttrs.get(TRACK);
		return ti == null ? -1 : JkConverter.stringToInteger(ti.getInfo().replaceAll("/.*", ""), -1);
	}
	private int getGenreAttr() {
		TextInfo ti = textInfoAttrs.get(GENRE);
		return ti == null ? -1 : ID3Genre.getByName(ti.getInfo()).getGenreNum();
	}

	private void extractMP3Attributes() {
		for(MP3Attribute attr : MP3Attribute.values()) {
			List<IFrameData> distinct = new ArrayList<>();

			for(TAGv2 tagv2 : mp3File.getTAGv2List()) {
				tagv2.getFrameList().stream()
					.filter(f -> MP3Attribute.getFromFrame(f) == attr)
					.map(ID3v2Frame::getFrameData)
					.forEach(f -> {
						IFrameData fdata = (IFrameData) f;
						List<IFrameData> filtered = JkStreams.filter(distinct, fdata::isDataDuplicated);
						if(filtered.isEmpty()) {
							distinct.add(fdata);
						}
					});
			}

			if(distinct.isEmpty()) {
				String attrValue = getAttributeFromTAGv1(attr);
				if(StringUtils.isNotBlank(attrValue)) {
					distinct.add(new TextInfo(attrValue));
				}
			}

			if(!distinct.isEmpty()) {
				if(attr == COVER) {
					cover = (Picture)distinct.get(0);
				} else if(attr == PICTURE) {
					pictures.addAll(JkStreams.map(distinct, f -> (Picture)f));
				} else if(attr == LYRICS) {
					lyrics = (Lyrics) distinct.get(0);
				} else if(attr == OTHER_LYRICS) {
					otherLyrics.addAll(JkStreams.map(distinct, f -> (Lyrics)f));
				} else {  // text info attribute
					textInfoAttrs.put(attr, (TextInfo)distinct.get(0));
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
