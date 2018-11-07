package xxx.joker.apps.tagmod.downloader.lyrics.model;

import xxx.joker.libs.core.repository.entity.JkDefaultEntity;
import xxx.joker.libs.core.repository.entity.JkEntityField;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.Arrays;

public class WebLyrics extends JkDefaultEntity {

    @JkEntityField(index = 0)
    private Integer track;
    @JkEntityField(index = 1)
    private String title;
    @JkEntityField(index = 2)
    private String artist;
    @JkEntityField(index = 3)
    private String album;
    @JkEntityField(index = 4)
    private String year;
    @JkEntityField(index = 5)
    private String lyricsText;


    @Override
    public String getPrimaryKey() {
        return JkStreams.join(Arrays.asList(artist, year, album, ""+track, title), "-");
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLyricsText() {
        return lyricsText;
    }

    public void setLyricsText(String lyricsText) {
        this.lyricsText = lyricsText;
    }

    @Override
    public String toString() {
        return getPrimaryKey();
    }
}
