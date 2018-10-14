package xxx.joker.apps.tagmod.model.id3v1;

/**
 * Created by f.barbano on 28/02/2018.
 */
public interface TAGv1 {

	int getRevision();
	String getTitle();
	String getArtist();
	String getAlbum();
	String getYear();
	String getComments();
	int getTrack();
	int getGenre();

	void setRevision(int revision);
	void setTitle(String title);
	void setArtist(String artist);
	void setAlbum(String album);
	void setYear(String year);
	void setComments(String comments);
	void setTrack(int track);
	void setGenre(int genre);

	int getTagLength();

	byte[] toBytes();

}
