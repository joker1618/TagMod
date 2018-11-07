package stuff;

import common.TestUtil;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class CopyLyricsStuff {

    @Test
    public void copyLyrics() {
        final Path root = TestUtil.HOME.resolve("Desktop\\jk music\\Vasco Rossi\\final\\1978 ...Ma cosa vuoi che sia una canzone");
        final boolean doCopy = true;

        List<Path> files = JkFiles.findFiles(root, true, TmFormat::isMP3File);
        List<TagmodFile> tmFiles = JkStreams.map(files, TagmodFile::new);

        LyricsModel model = LyricsModel.getInstance();
        TreeSet<WebLyrics> lyrics = model.getWebLyrics();
        Map<Path, WebLyrics> okMap = new TreeMap<>();

        for(TagmodFile tmfile : tmFiles) {
            TagmodAttributes tmattr = tmfile.getTagmodAttributes();
            List<WebLyrics> filterLyrics = lyrics.stream()
                    .filter(wl -> wl.getArtist().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.ARTIST)).getInfo()))
                    .filter(wl -> wl.getYear().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.YEAR)).getInfo()))
                    .filter(wl -> wl.getAlbum().equalsIgnoreCase(((TextInfo) tmattr.getFrameData(MP3Attribute.ALBUM)).getInfo()))
                    .filter(wl -> wl.getTrack()==ID3SetPos.parse(((TextInfo) tmattr.getFrameData(MP3Attribute.TRACK)).getInfo()).getIndex())
                    .collect(Collectors.toList());

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
}
