package stuff;

import common.TestUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import xxx.joker.apps.tagmod.common.TagmodConst;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.apps.tagmod.model.facade.TagmodAttributes;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.standard.ID3SetPos;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.TextInfo;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.core.utils.JkConsole;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class CopyStuff {

    @Test
    public void copyLyrics() {
        final Path root = TestUtil.HOME.resolve("Desktop\\jk music\\Vasco Rossi\\2lyrics");
        final boolean doCopy = false;
        final boolean checkOnlyTitle = false;

        List<Path> files = JkFiles.findFiles(root, true, TmFormat::isMP3File);
        List<TagmodFile> tmFiles = JkStreams.map(files, TagmodFile::new);

        LyricsModel model = LyricsModel.getInstance();
        TreeSet<WebLyrics> lyrics = model.getWebLyrics();
        Map<Path, WebLyrics> okMap = new TreeMap<>();

        for(TagmodFile tmfile : tmFiles) {
            TagmodAttributes tmattr = tmfile.getTagmodAttributes();
            List<WebLyrics> filterLyrics;

            if(!checkOnlyTitle) {
                filterLyrics = lyrics.stream()
                        .filter(wl -> wl.getArtist().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.ARTIST)).getInfo()))
                        .filter(wl -> wl.getYear().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.YEAR)).getInfo()))
                        .filter(wl -> wl.getAlbum().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.ALBUM)).getInfo()))
                        .filter(wl -> wl.getTrack() == ID3SetPos.parse(((TextInfo) tmattr.getFrameData(MP3Attribute.TRACK)).getInfo()).getIndex())
                        .collect(Collectors.toList());
            } else {
                filterLyrics = lyrics.stream().filter(wl -> wl.getTitle().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.TITLE)).getInfo())).collect(Collectors.toList());
            }

            Path fpath = tmfile.getMp3File().getFilePath();
            if(filterLyrics.isEmpty()) {
                display("NO lyrics found for %s", fpath);
            } else if(filterLyrics.size() != 1) {
                display("%d lyrics found for %s", filterLyrics.size(), fpath);
                filterLyrics.forEach(fl -> display("\t%s", fl));
            } else {
                okMap.put(fpath, filterLyrics.get(0));
            }
        }

        display("%d/%d lyrics associated.", okMap.size(), tmFiles.size());

        if(doCopy) {
            okMap.forEach((k, v) -> {
                Path lpath = Paths.get(k.toString().replaceAll("mp3$", "lyrics"));
                JkFiles.writeFile(lpath, v.getLyricsText(), true);
            });
        }

        display("\nEND lyrics copy");
    }


    @Test
    public void displayValues() {
        boolean showTitlesOnly = false;
        String year = "";
        String artist = "Vasco Rossi";
        String album = "";

        LyricsModel model = LyricsModel.getInstance();

        List<WebLyrics> llist = model.getWebLyrics().stream()
                .filter(wl -> artist.isEmpty() || wl.getArtist().equalsIgnoreCase(artist))
                .filter(wl -> year.isEmpty() || wl.getYear().equalsIgnoreCase(year))
                .filter(wl -> album.isEmpty() || wl.getAlbum().equalsIgnoreCase(album))
                .sorted()
                .collect(Collectors.toList());

        display("%s - %s - %s", artist, year, album);
        if(showTitlesOnly) {
            llist.forEach(l -> display("%s", l.getTitle()));
        } else {
            llist.forEach(l -> display("%s", l));
        }

        llist.stream().map(WebLyrics::getAlbum).distinct().forEach(l -> display("%s", l));

    }

    @Test
    public void copyTitlesFiles() {
        final Path root = TestUtil.HOME.resolve("Desktop\\jk music\\Vasco Rossi\\1wmod");
        final boolean doCopy = true;
        final boolean verbose = false;
        LyricsModel model = LyricsModel.getInstance();

        List<Path> folders = JkFiles.findFiles(root, true, TmFormat::isMP3File).stream()
                .map(p -> p.toAbsolutePath().normalize().getParent())
                .sorted().distinct().collect(Collectors.toList());

        folders.forEach(f -> display(f.toString()));

        display("Root folder is %s\n", root);

        for(Path folder : folders) {
            String fname = folder.getFileName().toString();
            String year = fname.replaceAll(" .*", "");
            String album = fname.replaceAll("^[0-9]{4} ", "");

            List<String> titles = model.getWebLyrics().stream()
                .filter(wl -> year.isEmpty() || wl.getYear().equalsIgnoreCase(year))
                .filter(wl -> album.isEmpty() || StringUtils.startsWithIgnoreCase(album, wl.getAlbum()))
                .sorted(Comparator.comparing(WebLyrics::getTrack))
                .map(WebLyrics::getTitle)
                .collect(Collectors.toList());

            int numSongs = JkFiles.findFiles(folder, false, TmFormat::isMP3File).size();

            if(doCopy)      JkFiles.writeFile(folder.resolve("titles.txt"), titles, true);
            display("%s titles file for %s", doCopy ? "Created":"Sample", folder.getFileName());
            display("Song associated: %d/%d  %s", titles.size(), numSongs, titles.size() == numSongs ? "OK":"WRONG");
            if(verbose)     titles.forEach(t -> display("  %s",t));
        }

    }
}
