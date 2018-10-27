package xxx.joker.apps.tagmod.model.id3v1;


import xxx.joker.apps.tagmod.exceptions.InvalidArgError;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.BytesScanner;

/**
 * Created by f.barbano on 01/03/2018.
 */
public class TAGv1Impl implements TAGv1 {

	private int revision = -1;
	private String title = "";
	private String artist = "";
	private String album = "";
	private String year = "";
	private String comments = "";
	private int track = -1;
	private int genre = -1;

	public TAGv1Impl() {

	}

	public static TAGv1 createFromBytes(byte[] bytes) {
		if(bytes.length != 128) {
			throw new InvalidArgError("ID3v1 tag size expected 128, found %d", bytes.length);
		}

		BytesScanner scanner = BytesScanner.getScanner(bytes);

		String heading = scanner.getString(0, 3);
		if(!heading.equals(ID3Specs.ID3v1_HEADING)) {
			return null;
		}
		scanner.skip(3);

		TAGv1Impl tag = new TAGv1Impl();
		tag.setTitle(scanner.nextString(30));
		tag.setArtist(scanner.nextString(30));
		tag.setAlbum(scanner.nextString(30));
		tag.setYear(scanner.nextString(4));

		if(bytes[125] == 0) {
			tag.setRevision(1);
			tag.setComments(scanner.nextString(28));
			scanner.skip(1);
			tag.setTrack(scanner.nextByteInt());
		} else {
			tag.setRevision(0);
			tag.setComments(scanner.nextString(30));
			tag.setTrack(-1);
		}

		tag.setGenre(scanner.nextByteInt());

		return tag;
	}

	@Override
	public int getRevision() {
		return revision;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public String getAlbum() {
		return album;
	}

	@Override
	public String getYear() {
		return year;
	}

	@Override
	public String getComments() {
		return comments;
	}

	@Override
	public int getTrack() {
		return track;
	}

	@Override
	public int getGenre() {
		return genre;
	}

	@Override
	public int getTagLength() {
		return ID3Specs.ID3v1_TAG_LENGTH;
	}

	@Override
	public byte[] toBytes() {
		ByteBuilder bb = new ByteBuilder();
		bb.add(ID3Specs.ID3v1_HEADING);
		bb.add(title, 30);
		bb.add(artist, 30);
		bb.add(album, 30);
		bb.add(year, 4);
		if (revision == 1) {
			bb.add(String.format("%-28s", comments), 28);
			bb.add(0x00);
			bb.add(track);
		} else {
			bb.add(String.format("%-30s", comments), 30);
		}
		bb.add(genre);
		return bb.build();
	}


	@Override
	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public void setAlbum(String album) {
		this.album = album;
	}

	@Override
	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public void setTrack(int track) {
		this.track = track;
	}

	@Override
	public void setGenre(int genre) {
		this.genre = genre;
	}
}
