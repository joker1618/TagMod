package xxx.joker.apps.tagmod.downloader.lyrics;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.util.Arrays;
import java.util.TreeSet;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class CommonLauncher {

    @Test
    public void modelStats() {
        TreeSet<WebLyrics> data = LyricsModel.getInstance().getData(WebLyrics.class);
        JkColumnFmtBuilder colb = new JkColumnFmtBuilder();
        JkStreams.toMap(data, WebLyrics::getArtist).forEach((k,v) -> colb.addLines(strf("%s:|%d", k, v.size())));
        colb.addLines(strf("TOTAL:|%d", data.size()));
        display(colb.toString("|", 3));
    }

}
