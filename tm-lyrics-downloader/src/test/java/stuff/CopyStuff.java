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
import xxx.joker.libs.core.utils.JkConverter;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class CopyStuff {

    @Test
    public void createTitlesFiles() {
        final Path root = TestUtil.HOME.resolve("Desktop\\jk music\\The Offspring");
        String artist = "The Offspring";
        final boolean doCopy = true;

        LyricsModel model = LyricsModel.getInstance();

        List<Path> folders = JkFiles.findFiles(root, true, TmFormat::isMP3File).stream()
                .map(JkFiles::getParent)
                .sorted().distinct().collect(Collectors.toList());

        display("ROOT FOLDER is %s%s\n", root, doCopy?"":"  (not create files)");

        for(Path folder : folders) {
            display("Folder %s", folder);

            String fname = folder.getFileName().toString();
            Matcher matcher = Pattern.compile("^([0-9]{4}) (.*)").matcher(fname);
            if(!matcher.matches())  {
                display("  ... skip");
                continue;
            }

            String year = matcher.group(1);
            String album = matcher.group(2).trim();

            List<String> titles = model.getWebLyrics().stream()
                .filter(wl -> wl.getArtist().equalsIgnoreCase(artist))
                .filter(wl -> wl.getYear().equalsIgnoreCase(year))
                .filter(wl -> StringUtils.startsWithIgnoreCase(album, wl.getAlbum()))
                .sorted(Comparator.comparing(WebLyrics::getTrack))
                .map(WebLyrics::getTitle)
                .collect(Collectors.toList());

            List<Path> files = JkFiles.findFiles(folder, false, TmFormat::isMP3File);
            display("  Titles: %d/%d", titles.size(), files.size());
//            titles.forEach(t -> display("  %s",t));
            boolean resTitles = titles.size() == files.size();

//            if(doCopy) {
            if(doCopy && resTitles) {
                Path outPath = folder.resolve("titles.txt");
                JkFiles.writeFile(outPath, titles, true);
                display("  Created file %s", outPath);
            }
        }

    }

    @Test
    public void createLyricsFiles() {
        final Path root = TestUtil.HOME.resolve("Desktop\\jk music\\The Offspring");
        String artist = "The Offspring";
        final boolean doCopy = true;

        LyricsModel model = LyricsModel.getInstance();

        List<Path> folders = JkFiles.findFiles(root, true, TmFormat::isMP3File).stream()
                .map(JkFiles::getParent)
                .sorted().distinct().collect(Collectors.toList());

        folders.forEach(f -> display(f.toString()));

        display("ROOT FOLDER is %s%s\n", root, doCopy?"":"  (not create files)");

        for(Path folder : folders) {
            display("Folder %s", folder);

            String fname = folder.getFileName().toString();
            Matcher matcher = Pattern.compile("^([0-9]{4}) (.*)").matcher(fname);
            if(!matcher.matches())  {
                display("  ... skip");
                continue;
            }

            String year = matcher.group(1);
            String album = matcher.group(2).trim();

            List<Path> files = JkFiles.findFiles(folder, false, TmFormat::isMP3File);
            Map<Path, WebLyrics> lyricsMap = new HashMap<>();

            for(Path file : files) {
                matcher = Pattern.compile("^([0-9]*) (.*)").matcher(JkFiles.getFileName(file));
                if (!matcher.matches()) {
                    display("  ... skip %s", file);
                    continue;
                }
//                int trackNum = JkConverter.stringToInteger(matcher.group(1));
                String strTitle = matcher.group(2).trim();

                List<WebLyrics> titles = model.getWebLyrics().stream()
                        .filter(wl -> wl.getArtist().equalsIgnoreCase(artist))
                        .filter(wl -> wl.getYear().equalsIgnoreCase(year))
                        .filter(wl -> StringUtils.startsWithIgnoreCase(album, wl.getAlbum()))
//                        .filter(wl -> wl.getTrack() == trackNum)
                        .filter(wl -> wl.getTitle().equals(strTitle))
                        .collect(Collectors.toList());

                if(titles.isEmpty()) {
                    display("  No lyrics for %s", file);
                } else if(titles.size() > 1) {
                    display("  %d lyrics for %s", titles.size(), file);
//                    titles.forEach(wl -> display("%s\n%s\n", wl.toString(), wl.getLyricsText()));
                } else {
                    lyricsMap.put(file, titles.get(0));
                }
            }

            display("  Lyrics: %d/%d", lyricsMap.size(), files.size());
            boolean resLyrics = lyricsMap.size() == files.size();

//            if(doCopy) {
            if(doCopy && resLyrics) {
                for (Path file : lyricsMap.keySet()) {
                    Path outPath = folder.resolve(JkFiles.getFileName(file) + ".lyrics");
                    JkFiles.writeFile(outPath, lyricsMap.get(file).getLyricsText(), true);
                    display("  Created file %s", outPath);
                }
            }
        }

    }
}
