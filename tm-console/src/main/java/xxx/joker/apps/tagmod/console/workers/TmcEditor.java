package xxx.joker.apps.tagmod.console.workers;

import xxx.joker.apps.tagmod.common.TagmodConst;
import xxx.joker.apps.tagmod.console.config.TmcConfig;
import xxx.joker.apps.tagmod.model.facade.TagmodAttributes;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.facade.TagmodSign;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3SetPos;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.language.JkLanguage;
import xxx.joker.libs.language.JkLanguageDetector;
import xxx.joker.libs.core.utils.JkConverter;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcEditor {

    private static final String AUTO_LYRICS_EXT = "lyrics";

    // remove all frames before set new ones
    private boolean clear;

    private boolean noSign;

    private boolean autoTiTle;
    private boolean autoTrack;
    private boolean autoLyrics;

    private String title;
    private String artist;
    private String album;
    private Integer year;
    private ID3SetPos track;
    private ID3Genre genre;
    private ID3SetPos cdPos;
    private Picture cover;
    private Lyrics lyrics;

    private Set<MP3Attribute> toDeletes = new HashSet<>();

    public TmcEditor() {

    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public void setNoSign(boolean noSign) {
        this.noSign = noSign;
    }

    public void setAutoTiTle(boolean autoTiTle) {
        this.autoTiTle = autoTiTle;
    }
    public void setAutoTrack(boolean autoTrack) {
        this.autoTrack = autoTrack;
    }
    public void setAutoLyrics(boolean autoLyrics) {
        this.autoLyrics = autoLyrics;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setTrack(ID3SetPos track) {
        this.track = track;
    }
    public void setGenre(ID3Genre genre) {
        this.genre = genre;
    }
    public void setCdPos(ID3SetPos cdPos) {
        this.cdPos = cdPos;
    }
    public void setCover(Picture cover) {
        this.cover = cover;
    }
    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
    }

    public void deleteTitle(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.TITLE);
        else        toDeletes.remove(MP3Attribute.TITLE);
    }
    public void deleteArtist(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.ARTIST);
        else        toDeletes.remove(MP3Attribute.ARTIST);
    }
    public void deleteAlbum(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.ALBUM);
        else        toDeletes.remove(MP3Attribute.ALBUM);
    }
    public void deleteYear(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.YEAR);
        else        toDeletes.remove(MP3Attribute.YEAR);
    }
    public void deleteTrack(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.TRACK);
        else        toDeletes.remove(MP3Attribute.TRACK);
    }
    public void deleteGenre(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.GENRE);
        else        toDeletes.remove(MP3Attribute.GENRE);
    }
    public void deleteCdPos(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.CD_POS);
        else        toDeletes.remove(MP3Attribute.CD_POS);
    }
    public void deleteCover(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.COVER);
        else        toDeletes.remove(MP3Attribute.COVER);
    }
    public void deleteLyrics(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.LYRICS);
        else        toDeletes.remove(MP3Attribute.LYRICS);
    }
    public void deletePictures(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.PICTURES);
        else        toDeletes.remove(MP3Attribute.PICTURES);
    }
    public void deleteOtherLyrics(boolean delete) {
        if(delete)  toDeletes.add(MP3Attribute.OTHER_LYRICS);
        else        toDeletes.remove(MP3Attribute.OTHER_LYRICS);
    }

    public boolean editTagmodFile(TagmodFile tmFile, Integer version, TxtEncoding encoding, Integer padding) throws Exception {
        TagmodAttributes tmAttribs = new TagmodAttributes();

        // Input attributes
        tmAttribs.setAttributeString(MP3Attribute.TITLE, title);
        tmAttribs.setAttributeString(MP3Attribute.ARTIST, artist);
        tmAttribs.setAttributeString(MP3Attribute.ALBUM, album);
        if(year != null)    tmAttribs.setAttributeString(MP3Attribute.YEAR, ""+year);
        if(track != null)   tmAttribs.setAttributeString(MP3Attribute.TRACK, track.toString());
        if(genre != null)   tmAttribs.setAttributeString(MP3Attribute.GENRE, genre.getGenreName());
        if(cdPos != null)   tmAttribs.setAttributeString(MP3Attribute.CD_POS, cdPos.toString());
        tmAttribs.setAttribute(MP3Attribute.COVER, cover);
        tmAttribs.setAttribute(MP3Attribute.LYRICS, lyrics);

        // Auto attributes
        if(autoTiTle || autoTrack || autoLyrics) {
            AutoValues autoValues = new AutoValues(tmFile, autoTrack);
            if(autoTiTle) {
                tmAttribs.setAttributeString(MP3Attribute.TITLE, autoValues.title);
            }
            if(autoTrack && autoValues.track != null) {
                tmAttribs.setAttributeString(MP3Attribute.TRACK, autoValues.track.toString());
            }
            if(autoLyrics) {
                Lyrics lyrics = parseLyrics(autoValues.lyricsPath);
                tmAttribs.setAttribute(MP3Attribute.LYRICS, lyrics);
            }
        }

        // Legacy attributes
        if(!clear) {
            TagmodAttributes legacy = tmFile.getTagmodAttributes();
            tmAttribs.addAllAttributes(legacy);
        }

        // Remove attributes to delete
        toDeletes.forEach(tmAttribs::removeAttribute);

        // Output formats
        TagmodSign sign = tmFile.getTagmodSign();
        TxtEncoding enc = encoding != null ? encoding : (sign != null && sign.isValid() ? sign.getEncoding() : TmcConfig.DEFAULT_OUTPUT_ENCODING);
        int ver = version != null ? version : (sign != null && sign.isValid() ? sign.getVersion() : TmcConfig.DEFAULT_OUTPUT_VERSION);

        return tmFile.persistChanges(tmAttribs, ver, enc, padding, !noSign);
    }

    private Lyrics parseLyrics(Path lyricsPath) throws IOException {
        if(!Files.exists(lyricsPath))    return null;
        String lyricsText = JkStreams.join(Files.readAllLines(lyricsPath), "\n");
        JkLanguage lan = JkLanguageDetector.detectLanguage(lyricsText);
        lan = lan == null ? JkLanguage.ENGLISH : lan;
        return new Lyrics(lan, TagmodConst.LYRICS_DESCR, lyricsText);
    }

    private static class AutoValues {
        private ID3SetPos track;
        private String title;
        private Path lyricsPath;

        private static Map<Path, Integer> trackTotalMap = new HashMap<>();

        AutoValues(TagmodFile tmFile, boolean setTrackTotal) {
            Path filePath = tmFile.getMp3File().getFilePath();
            Path fileParent = JkFiles.getParent(filePath);

            String fileName = JkFiles.getFileName(filePath).trim();
            int idxSpace = fileName.indexOf(' ');
            if(idxSpace == -1) {
                title = fileName;
            } else {
                Integer trackNum = JkConverter.stringToInteger(fileName.substring(0, idxSpace));
                if(trackNum == null) {
                    title = fileName;
                } else {
                    title = fileName.substring(idxSpace).trim();
                    Integer tot = null;
                    if(setTrackTotal) {
                        tot = trackTotalMap.get(fileParent);
                        if(tot == null) {
                            tot = JkFiles.findFiles(fileParent, false, TmFormat::isMP3File).size();
                            trackTotalMap.put(fileParent, tot);
                        }
                    }
                    track = new ID3SetPos(trackNum, tot);
                }
            }

            lyricsPath = fileParent.resolve(strf("%s.%s", fileName, AUTO_LYRICS_EXT));
        }

    }
}
