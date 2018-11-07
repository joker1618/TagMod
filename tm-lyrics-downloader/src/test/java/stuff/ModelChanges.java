package stuff;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class ModelChanges {

    @Test
    public void change() {
        LyricsModel model = LyricsModel.getInstance();
        List<WebLyrics> wlList = JkStreams.filter(model.getWebLyrics(), wl -> wl.getArtist().startsWith("Vasco") && Integer.parseInt(wl.getYear()) == 1982);
        wlList.forEach(wl -> display("%s", wl));
    }
}
